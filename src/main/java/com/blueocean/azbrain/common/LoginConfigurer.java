package com.blueocean.azbrain.common;

import com.blueocean.azbrain.common.kcode.KCodeService;
import com.blueocean.azbrain.common.kcode.ProdKCodeService;
import com.blueocean.azbrain.common.kcode.TestKCodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfigurer implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor logInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    @Profile("dev")
    public KCodeService getDevKCodeService(){
        return new TestKCodeService();
    }

    @Bean
    @Profile("test")
    public KCodeService getTestKCodeService(){
        return new TestKCodeService();
    }

    @Bean
    @Profile("prod")
    public KCodeService getProdKCodeService(){
        return new ProdKCodeService();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("access_token")
                .allowCredentials(true).maxAge(3600);
    }
}
