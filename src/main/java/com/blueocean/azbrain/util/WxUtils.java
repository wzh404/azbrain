package com.blueocean.azbrain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WxUtils {
    private final static Logger logger = LoggerFactory.getLogger(WxUtils.class);
    private final static String WxUrl = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";
    public  static String SESSION_KEY = "";
    private static String uri = "http://eaopen.astrazeneca.com/api/sendmessage";
    private static String key = "792e1f2f428e6bb9";
    private static String secret = "8C33EF4E69FA9AC7720E486047241420";

    public static boolean wxMessage(String kcode, String content){
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            HttpPost httpPost = new HttpPost(uri);
            List<NameValuePair> pair = new ArrayList<>();
            pair.add(new BasicNameValuePair("key", key));
            String date = LocalDateTime.now().format(df);
            pair.add(new BasicNameValuePair("date", date));
            pair.add(new BasicNameValuePair("sign", CryptoUtil.md5(key, date, secret)));
            pair.add(new BasicNameValuePair("userid", kcode));
            pair.add(new BasicNameValuePair("content", content));

            httpPost.setEntity(new UrlEncodedFormEntity(pair, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String body = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSON.parseObject(body);
                    int status = jsonObject.getInteger("status");
                    EntityUtils.consume(entity);
                    logger.info("status is {} - {}", status, jsonObject.getString("errmsg"));
                    return status == 1;
                } else {
                    logger.warn("response empty body");
                }
            } else {
                logger.error("send message to wx failed");
            }
        } catch (IOException e) {
            logger.error("get openid exception", e);
        }

        return false;
    }

    /**
     * 根据小程序临时code，获取登录者的openid
     *
     * @param appid
     * @param secret
     * @param code
     *
     * @return openid
     */
    public static Optional<String> getOpenId(String appid, String secret, String code) {
        StringBuilder url = new StringBuilder(WxUrl);
        url.append("&appid=");
        url.append(appid);
        url.append("&secret=");
        url.append(secret);
        url.append("&js_code=");
        url.append(code);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(url.toString());
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
                    WxUtils.SESSION_KEY = jsonObject.getString("session_key");
                    return Optional.ofNullable(jsonObject.getString("openid"));
                } else {
                    logger.warn("wx errcode is {}", errcode);
                }
            }
        } catch (IOException e) {
            logger.error("get openid exception", e);
        }

        return Optional.empty();
    }

    /**
     *
     * @param encryptedData
     * @param sessionkey
     * @param iv
     * @return
     */
    public static String getUserInfo(String encryptedData, String sessionkey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            //Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));

            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
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
