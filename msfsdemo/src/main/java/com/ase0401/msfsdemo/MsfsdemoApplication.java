package com.ase0401.msfsdemo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.ase0401.msfsdemo.service.InitializerService;

/**
 * @author stela This is the main class, used by Spring Boot to initialize the
 *         application.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ase0401.msfsdemo")
@EnableScheduling
public class MsfsdemoApplication {

	@Autowired
	InitializerService initializer;


	public static void main(String[] args) {
		SpringApplication.run(MsfsdemoApplication.class, args);

	}

	@PostConstruct
	private void init() {
		initializer.initialize();
	}

	@Bean
	@Scope("prototype")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
