package com.shaoxia.todo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-05-30 17:04
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.shaoxia.todo.mapper")
public class TodoServiceProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoServiceProviderApplication.class,args);
	}
}
