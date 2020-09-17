package me.lqw.blog8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
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
 * @since 2.3 添加 @EnableScheduling
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Blog implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /**
     * 构造方法
     */
    public Blog() {
        super();
    }

    /**
     * 主入口
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(Blog.class, args);
    }

    /**
     * 启动完成后回调
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner check() {
        return args -> {
            logger.info("Blog Starting success");
        };
    }

    /**
     * Context 刷新时调用
     * @param event event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        logger.info("Blog onApplicationEvent() context refreshed!");
    }
}
