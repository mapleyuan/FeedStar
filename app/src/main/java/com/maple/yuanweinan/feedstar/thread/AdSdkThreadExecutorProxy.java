package com.maple.yuanweinan.feedstar.thread;

import android.os.Handler;
import android.os.Looper;

/**
 *	目前只用于主线程的调用
 */
public class AdSdkThreadExecutorProxy {
	/**
	 * 主线程Handler
	 */
	private static Handler sMainHandler;

	/**
	 * 初始化
	 */
	public static void init() {
		if (sMainHandler != null) {
			return;
		}
		sMainHandler = new Handler(Looper.getMainLooper());
	}

	/**
	 * 取消指定的任务
	 * @param task
	 */
	public static void cancel(final Runnable task) {
		if (sMainHandler == null) {
			return;
		}
		sMainHandler.removeCallbacks(task);
	}

	/**
	 * 销毁对象
	 */
	public static void destroy() {
		if (sMainHandler == null) {
			return;
		}
		sMainHandler.removeCallbacksAndMessages(null);
	}

	/**
	 * 提交一个Runable到主线程队列
	 */
	public static void runOnMainThread(Runnable r) {
		if (sMainHandler == null) {
			init();
		}
		sMainHandler.post(r);
	}

	/**
	 * 提交一个Runable到主线程队列
	 */
	public static void runOnMainThread(Runnable r, long delay) {
		if (sMainHandler == null) {
			init();
		}
		sMainHandler.postDelayed(r, delay);
	}
}
