package com.blueocean.azbrain;

import com.blueocean.azbrain.util.CryptoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApplicationTests {

    @Test
    public void passwordTest(){
        String pwd = CryptoUtil.signature("admin");
        System.out.println(pwd);
    }
}
