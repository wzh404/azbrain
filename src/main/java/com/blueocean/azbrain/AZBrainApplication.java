package com.blueocean.azbrain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AZBrainApplication{

	public static void main(String[] args) {
		SpringApplication.run(AZBrainApplication.class, args);
	}

	@Bean("smsCache")
	public Cache<String, String> smsCache(){
		return CacheBuilder.newBuilder()
				.initialCapacity(1000)
				.expireAfterWrite(5, TimeUnit.MINUTES)
				.build();
	}
}
