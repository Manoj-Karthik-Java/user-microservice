package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser;

import feign.Logger;
import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class PhotoAppApiUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUserApplication.class, args);
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}
	@Bean
	public HttpExchangeRepository httpExchangeList(){
		return new InMemoryHttpExchangeRepository();
	}
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

//	Below code is specific for feign. It is used to log the exchange details
//	Logger.level.FULL is used to log full details of exchange
//	Logger.level.BASIC is used to log basic details like request, url and response
	@Bean
	Logger.Level feignLoggerLevel(){
		return Logger.Level.FULL;
	}

}
