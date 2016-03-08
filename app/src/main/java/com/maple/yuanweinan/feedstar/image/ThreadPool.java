package com.maple.yuanweinan.feedstar.image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类
 * @author matt
 *
 */
public class ThreadPool {
    public static final int THREADPOOL_CAPACITY_DEF = 2; //默认线程池容量
    private ExecutorService mThreadPool; //线程池
    private int mThreadCapacity;
    
    public ThreadPool() {
    	this(THREADPOOL_CAPACITY_DEF);
    }
    
    /**
     * 线程池数量
     * @param threadCapacity
     */
    public ThreadPool(int threadCapacity) {
    	mThreadCapacity = threadCapacity;
    	mThreadPool = Executors.newFixedThreadPool(mThreadCapacity);
    }
    
    public void submit(Runnable r) {
        open();
        mThreadPool.submit(r);
    }
    
    /**
     * 关闭线程池,清除已submit但未执行的任务
     */
    public void shutDown() {
//      mThreadPool.shutdown();
        mThreadPool.shutdownNow();
    }
    
    /**
     * 检查并开启线程池
     */
    private void open() {
        if (null == mThreadPool || mThreadPool.isShutdown()) {
            mThreadPool = Executors.newFixedThreadPool(mThreadCapacity);
        }
    }
}
