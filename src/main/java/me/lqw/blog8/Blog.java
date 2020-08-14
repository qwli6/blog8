package me.lqw.blog8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Blog 启动类
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
@EnableCaching
@SpringBootApplication
public class Blog {

	public static void main(String[] args) {
		SpringApplication.run(Blog.class, args);
	}

}
