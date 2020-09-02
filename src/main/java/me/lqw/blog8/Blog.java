package me.lqw.blog8;

import me.lqw.blog8.plugins.md.MarkdownParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * Blog 启动类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@EnableCaching
@SpringBootApplication
public class Blog {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ObjectProvider<MarkdownParser> markdownParserObjectProvider;

    public Blog(ObjectProvider<MarkdownParser> objectProvider) {
        this.markdownParserObjectProvider = objectProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(Blog.class, args);
    }


    @Bean
    public CommandLineRunner check() {
        return args -> {
            logger.info("Blog Starting success");
        };
    }
}
