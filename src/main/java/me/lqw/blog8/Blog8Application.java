package me.lqw.blog8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Blog8Application {

	public static void main(String[] args) {
		SpringApplication.run(Blog8Application.class, args);
	}

}
