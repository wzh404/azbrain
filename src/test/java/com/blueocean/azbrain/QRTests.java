package com.blueocean.azbrain;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QRTests {
    public static void main(String[] args) throws Exception {
        String access_token = "11_7NYEjfsFqiNde99La5hkUsIipLvZC2yKYn0t_2GclqPY9HTHpV5DeEEpI_kq8nKjVmrB18Pf5D12_RBzk4LZYNOblwRz_HCpuHqB9ytQKw6dFEjfXYJeJeOZpNIfvynSaDU4aB_QPJxDEOcfOSUfAGAFJL";
        {
            System.out.println(access_token);
            Map<String, Object> params = new HashMap<>();
            params.put("scene", "test");
            params.put("path", "pages/index/index");
            params.put("width", 430);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            String body = JSON.toJSONString(params);
            StringEntity entity;
            entity = new StringEntity(body);
            entity.setContentType("image/png");

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();

                File targetFile = new File("D:\\");
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                FileOutputStream out = new FileOutputStream("D:\\upload\\5.png");
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.flush();
                out.close();
                System.out.println("------------------completed--------");
            }
        }
    }
}
