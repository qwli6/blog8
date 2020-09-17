package me.lqw.blog8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAsync
public class ThreadPoolConfiguration {

    @Bean("blogThreadPoolExecutor")
    public Executor blogThreadPoolExecutor() {
        ThreadPoolTaskExecutor blogThreadPoolExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        blogThreadPoolExecutor.setCorePoolSize(10);
        //最大线程数
        blogThreadPoolExecutor.setMaxPoolSize(50);
        //队列容量
        blogThreadPoolExecutor.setQueueCapacity(200);
        //线程保活时间
        blogThreadPoolExecutor.setKeepAliveSeconds(60);
        //线程名称
        blogThreadPoolExecutor.setThreadNamePrefix("Blog-Thread-");

        //拒绝策略
        blogThreadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        blogThreadPoolExecutor.setWaitForTasksToCompleteOnShutdown(true);
        blogThreadPoolExecutor.setAwaitTerminationSeconds(60);

        blogThreadPoolExecutor.initialize();
        return blogThreadPoolExecutor;
    }
}
