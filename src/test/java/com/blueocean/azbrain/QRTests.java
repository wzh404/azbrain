package com.blueocean.azbrain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blueocean.azbrain.util.WxUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * {"errcode":40029,"errmsg":"invalid code, hints: [ req_id: tXhjOa06452030 ]"}
 * {"session_key":"OACTXbb8D8uwCq8gjMmVww==","expires_in":7200,"openid":"oZxbt0EcQeB2Kkj1OnKUbWhZ9Ctc"}
 *
 */
public class QRTests {
    private final static Logger logger = LoggerFactory.getLogger(QRTests.class);

    public static void main(String[] args)throws Exception{
        //openid();
        //String appid = "wx86faae0cc74bbf0e";
        //String secret = "5e5829e1b0b13484baf6a48c4783178a";
        //String jscode = "00178tqj2f6vuG0SO0qj2T9Cqj278tqu";
        //WxUtils.getOpenId(appid, secret, jscode);
        int[] ids = {209};
        for (int id :ids) {
            qr(id);
        }
    }


    public static Optional<String> getAccessToken(){
        String appid = "wxd77e96f996f72d94";
        String secret = "414bd1a065f27bb5a96eac6d34f3f6a9";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + secret;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            int httpStatusScode = response.getStatusLine().getStatusCode();
            if (httpStatusScode != 200) {
                logger.error("http resposne status is {}", httpStatusScode);
                return Optional.empty();
            }

            try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }

                logger.info(builder.toString());
                JSONObject jsonObject = JSON.parseObject(builder.toString());
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode == null) {
                    return Optional.ofNullable(jsonObject.getString("access_token"));
                } else {
                    logger.warn("wx errcode is {}", errcode);
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            logger.error("get openid exception", e);
            return Optional.empty();
        }
    }

    public static void qr(Integer id) throws Exception {
        String access_token = getAccessToken().get();
        {
            System.out.println(access_token);
            Map<String, Object> params = new HashMap<>();
            params.put("scene", "test");
            params.put("path", "pages/pensionAgency/main?id=" + id + "&km=0");
            params.put("width", 430);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            String body = JSON.toJSONString(params);

            StringEntity entity = new StringEntity(body);
            entity.setContentType("image/png");

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();

                File targetFile = new File("D:\\");
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                FileOutputStream out = new FileOutputStream("D:\\upload\\qr_" + id + ".png");
                byte[] buffer = new byte[10240];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer, 0, 10240)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.flush();
                out.close();
                System.out.println("------------------completed--------");
            }
        }
    }

    public static void openid(){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String appid = "wx86faae0cc74bbf0e";
        String secret = "5e5829e1b0b13484baf6a48c4783178a";
        String jscode = "011vzelF1URNf00dWllF1OK5lF1vzelU";
        String iv = "WbzFBYieKyBlVPyE3Oun6Q==";
        String endata = "rNpYmpoL2sgfoC2JkNh4uVtDnLDx3VMiIOMfiiy9L+biQT1r7otuye8/JoR98clxUS3awvM3vQOC6QNWXnl8FCqRvJ0L1pX6xvBBvZA/Nn393eHsXStlxIbGxreLKEkPVkAJLJQbtpjS1CQgjOxe5hrXIeRQz7wF4Svc8yyNDM9m6lS5ni0VmjigbZL5moYMXoHd3gijJvUx4s6SxT3rxOK0eW0/uFSID8fReTa7UchMPPjfRGC9eJWDVnnggTngrQ7qIhyTOZ6W0WIcZUuF6e8ImCmZ2VL6Tn3fv09ukvwzh0sDOQuiOZel+3FS1PoFn9pCGsc0FPR3MKZNzVxwa2rPhwfJkrqCkkynodjX/NgJvEj+VaW4cL7NBdlgQ2XmJmxaRkBWN8LzMo0pWhDZs3PnBff0w4lLJQoW1PqNa6ovq1El8ou0lJdUapbD91xRmv7qhBZLwYfjm7FZxBSZNzwgvy1RZEx6WuugYT1mEss=";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + jscode + "&grant_type=authorization_code";
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            System.out.println(builder);

            String sk = JSON.parseObject(builder.toString()).get("session_key").toString();

            getUserInfo(endata, sk, iv);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 获取信息
     */
    public static String getUserInfo(String encryptedData, String sessionkey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
//            int base = 16;
//            if (keyByte.length % base != 0) {
//                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
//                byte[] temp = new byte[groups * base];
//                Arrays.fill(temp, (byte) 0);
//                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
//                keyByte = temp;
//            }

            //Security.addProvider(new BouncyCastleProvider());
             Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));

                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    String result = new String(resultByte, "utf-8");
                    System.out.println(result);
                    return result;
                }
            System.out.println("failed");
                return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

