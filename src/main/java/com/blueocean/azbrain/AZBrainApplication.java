package com.blueocean.azbrain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AZBrainApplication{

	public static void main(String[] args) {
		SpringApplication.run(AZBrainApplication.class, args);
	}

	/*
	@Bean("smsCache")
	public Cache<String, String> smsCache(){
		return CacheBuilder.newBuilder()
				.initialCapacity(1000)
				.expireAfterWrite(5, TimeUnit.MINUTES)
				.build();
	}*/
}
