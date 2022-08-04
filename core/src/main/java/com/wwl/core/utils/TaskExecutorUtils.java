package com.wwl.core.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wwl
 * @date 2022/7/26 13:42
 * @desc 线程池工具类
 */
@Component
public class TaskExecutorUtils {

    private ThreadPoolTaskScheduler taskScheduler;

    @Bean
    public ThreadFactory threadFactory(){
        return new DefaultThreadFactory();
    }

    @Bean
    public AsyncTaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor(threadFactory());
    }

    @PostConstruct
    private void init(){
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setThreadFactory(threadFactory());
        this.taskScheduler.initialize();
    }

    public void startDelayTask(Runnable runnable,int delay){
        this.taskScheduler.schedule(runnable, DateUtils.addMilliseconds(new Date(),delay));
    }

    public void startTriggerTask(TriggerTask task){
        this.taskScheduler.schedule(task.getRunnable(),task.getTrigger());
    }


    public void startPeriodTask(Runnable runnable,long period){
        this.taskScheduler.scheduleAtFixedRate(runnable,period);
    }

    public static void recordMethodRunningTime(Runnable runnable) {
        long fromMills = System.currentTimeMillis();
        System.out.println("method start at:" + ConvertUtils.formatDatetime(new Date()));
        runnable.run();
        long endMills = System.currentTimeMillis();
        System.out.println("method end at:" + ConvertUtils.formatDatetime(new Date()) + ",total spend " + (endMills - fromMills)+" mills" );
    }

    @PreDestroy
    private void destroy(){
        if(this.taskScheduler!=null)
        {
            this.taskScheduler.shutdown();
        }
    }


    private final static AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(new DefaultThreadFactory());



    public static void executeTask(Runnable runnable){
        taskExecutor.execute(runnable);
    }

    public static void executeTask(Runnable runnable,long timeOut){
        taskExecutor.execute(runnable,timeOut);
    }

    /**
     * The default thread factory
     */
    public static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
            {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY)
            {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}
