package com.cyj.scheduledTask.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling  // 启用定时任务功能
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 创建线程池执行器
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // 设置核心线程池大小
        scheduler.setPoolSize(10);

        // 设置线程名前缀
        scheduler.setThreadNamePrefix("scheduled-task-");

        // 设置关闭策略：等待所有任务完成后再关闭
        scheduler.setWaitForTasksToCompleteOnShutdown(true);

        // 设置等待时间
        scheduler.setAwaitTerminationSeconds(60);

        // 初始化线程池
        scheduler.initialize();

        // 设置任务调度器
        taskRegistrar.setTaskScheduler(scheduler);
    }
}

