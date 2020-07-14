package me.lqw.blog8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author liqiwen
 * @version 1.0
 */
@Configuration
@EnableAsync
public class ThreadTaskPoolConfigAdapter {

    @Bean("DelayPerformOrderExecutor")
    public Executor delayPerformOrderExecutor(){
        ThreadPoolTaskExecutor delayPerformOrderExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        delayPerformOrderExecutor.setCorePoolSize(10);
        //最大线程数
        delayPerformOrderExecutor.setMaxPoolSize(50);
        //队列容量
        delayPerformOrderExecutor.setQueueCapacity(200);
        //线程保活时间
        delayPerformOrderExecutor.setKeepAliveSeconds(60);
        //线程名称
        delayPerformOrderExecutor.setThreadNamePrefix("DelayPerformOrderExecutor--");

//        taskExecutor.set
        //拒绝策略
        delayPerformOrderExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        delayPerformOrderExecutor.setWaitForTasksToCompleteOnShutdown(true);
        delayPerformOrderExecutor.setAwaitTerminationSeconds(60);

        delayPerformOrderExecutor.initialize();
        return delayPerformOrderExecutor;
    }
}
