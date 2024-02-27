package org.oakkar.chatapp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@Configuration
@EnableScheduling
@EnableAsync
public class ApplicationConfig {
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Yangon"));
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "taskExecutor")
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(500);
        executor.setKeepAliveSeconds(3200);
        executor.setThreadNamePrefix("PostThread-");
        executor.setQueueCapacity(1000);
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskScheduler")
    ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(100);
        taskScheduler.setThreadNamePrefix("ThreadPool-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}
