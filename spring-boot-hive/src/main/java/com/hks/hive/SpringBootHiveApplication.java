package com.hks.hive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.hks.hive")
public class SpringBootHiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHiveApplication.class, args);
	}
}
