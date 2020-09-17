package me.lqw.blog8;

import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.service.ArticleIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Blog 启动类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Blog implements ApplicationListener<ContextRefreshedEvent> {

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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
    }
}
