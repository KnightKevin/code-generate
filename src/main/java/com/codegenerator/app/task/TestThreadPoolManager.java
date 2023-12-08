package com.codegenerator.app.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestThreadPoolManager {

    // 线程池维护线程的最少数量
    private final static int CORE_POOL_SIZE = 2;

    /**
     * 线程池维护线程最大数量
     * */
    private final static int MAX_POOL_SIZE = 2;

    /**
     *
     * 线程池维护线程允许的空闲时间
     * */
    private final static int KEEP_ALIVE_TIME = 0;

    /**
     * 线程池所使用的缓冲队列大小
     * */
    private final static int WORK_QUEUE_SIZE = 2;


    /**
     * 用于存储在队列中的订单，防止重复提交，在真实场景中，可以用redis代替，验证重复
     * */
    Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    /**
     * 订单的缓冲队列， 当线程池满了，将订单存入到缓冲队列
     * */
    Queue<Object> msgQueue = new LinkedBlockingQueue<>();


    /**
     * 当线程池的容量满了会执行下面代码，将订单存入到缓冲队列中
     * */
    final RejectedExecutionHandler handler = (r, executor) -> {
        // 订单加如到缓冲队列
        msgQueue.offer(((BusinessThread) r).getAcceptStr());
        log.info("系统任务太忙了， 把此订单交给调度线程池逐一处理，订单号：{}", ((BusinessThread) r).getAcceptStr());
    };

    /**
     * 创建线程池
     * */
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(WORK_QUEUE_SIZE), handler);

    public void addOrders(String orderId) {
        log.info("添加订单到线程池， 订单号：{}", orderId);

        if (cacheMap.get(orderId) == null) {
            cacheMap.put(orderId, new Object());

            BusinessThread businessThread = new BusinessThread(orderId);

            threadPool.execute(businessThread);
        }
    }

    /**
     * 调度线程池。此线程支持钉是及周期性执行任务的需求
     * */
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    /**
     * 每秒执行一次，查看订单的缓冲队列是否有订单记录，有则重新加如到线程池
     * */
    final ScheduledFuture scheduledFuture = scheduler.scheduleAtFixedRate(()->{
        if (!msgQueue.isEmpty()) {

            // 当线程池的队列容量少于WORK_QUEUE_SIZE，则开始吧缓冲队列的订单加如到线程池中
            if ( threadPool.getQueue().size() < WORK_QUEUE_SIZE ) {
                String orderId = (String) msgQueue.poll();
                BusinessThread businessThread = new BusinessThread(orderId);
                threadPool.execute(businessThread);
                log.info("（调度线程池）缓冲队列出现订单业务，重新添加到线程池，订单号：{}", orderId);
            }
        }
    }, 0, 1, TimeUnit.SECONDS);


}
