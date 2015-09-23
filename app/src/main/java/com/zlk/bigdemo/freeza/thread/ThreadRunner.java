package com.zlk.bigdemo.freeza.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.zlk.bigdemo.freeza.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadRunner implements Callback {

	private static ThreadRunner sInstance;
	private ScheduledExecutorService mThreadPool;
	private Handler mMainHandler;
	private Map<Callable<?>, Task> mAllTasks = new HashMap<Callable<?>, Task>();

	private final int DEFAULT_MAX_THREADNUM = 3;

	public synchronized static ThreadRunner getInstance() {
		if (sInstance == null) {
			sInstance = new ThreadRunner();
		}
		return sInstance;
	}

	public ThreadRunner() {
		int coreNum = Util.getNumCores();
		mThreadPool = Executors.newScheduledThreadPool(coreNum
				* DEFAULT_MAX_THREADNUM, new ThreadFactory() {
			private AtomicInteger mCount = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable paramRunnable) {
				Thread thread = new Thread(paramRunnable);
				thread.setName("Freeza Thread-" + mCount.getAndIncrement());
				return thread;
			}

		});
		mMainHandler = new Handler(Looper.getMainLooper(), this);
	}

	public synchronized void start(Callable<?> callable, Callback callback) {
		if(callable == null){
			return;
		}
		Task task = new Task();
		task.isCancelled = false;
		task.mCallable = new CallableWrapper(callable);
		task.mCallback = callback;
		task.mFuture = mThreadPool.submit(task.mCallable);
		mAllTasks.put(callable, task);
	}
	
	public synchronized void cancelTask(Callable<?> callable,boolean force){
		Task task = mAllTasks.get(callable);
		if(task == null)
			return;
		
		task.isCancelled = true;
		
		if(null != task.mFuture){
			if(!task.mFuture.cancel(force)){
				return;
			}
		}
		mAllTasks.remove(callable);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		synchronized(this){
			CallableWrapper callableWrapper = (CallableWrapper) msg.obj;
			Task task = mAllTasks.get(callableWrapper.mRealCallable);
			if(task != null){
				if(false == task.isCancelled){
					Callback callback = task.mCallback;
					if(callback != null){
						Handler mainThreadHandler = new Handler(Looper.getMainLooper(),callback);
						Message message = Message.obtain();
						try {
							Object ret = task.mFuture.get();
							message.obj = ret;
							if(ret instanceof Throwable){
								message.obj = null;
							}
						} catch (InterruptedException e) {
						} catch (ExecutionException e) {
						}
						mainThreadHandler.sendMessage(message);
					}
				}
				mAllTasks.remove(callableWrapper.mRealCallable);
			}
		}
		return true;
	}

	class CallableWrapper implements Callable<Object> {
		private Callable<?> mRealCallable;

		public CallableWrapper(Callable<?> callable) {
			mRealCallable = callable;
		}

		@Override
		public Object call() throws Exception {
			if (mRealCallable == null)
				return null;

			Object ret = null;
			try {
				ret = mRealCallable.call();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.obj = this;
			mMainHandler.sendMessage(msg);
			return ret;
		}

	}

	class Task {
		private CallableWrapper mCallable;
		private Callback mCallback;
		private Future<?> mFuture;
		private boolean isCancelled;
	}
}
