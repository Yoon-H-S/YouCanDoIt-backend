package com.example.youcandoit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본으로 대기하는 쓰레드의 수
        executor.setMaxPoolSize(30); // 동시 동작하는 최대 쓰레드의 수
        executor.setQueueCapacity(50); // 최대 쓰레드의 수를 넘어 요청할 경우 대기 queue의 크기
        executor.setThreadNamePrefix("ASYNC-"); // 생성되는 쓰레드 접두사
        executor.initialize();
        return executor;
    }
}
