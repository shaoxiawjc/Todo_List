package com.shaoxia.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-22 18:49
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.shaoxia.user.mapper")
public class UserServiceProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceProviderApplication.class, args);
	}
}
