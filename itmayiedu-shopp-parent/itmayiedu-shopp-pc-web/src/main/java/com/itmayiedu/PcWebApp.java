package com.itmayiedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableEurekaServer
@SpringBootApplication
@EnableFeignClients
public class PcWebApp {
	public static void main(String[] args) {
		SpringApplication.run(PcWebApp.class, args);
	}

}
