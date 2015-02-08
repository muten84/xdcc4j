package it.biffi.jirc.bot.util;

import java.util.concurrent.ExecutionException;

/**
 * @author Luigi
 * 
 * @param <V>
 */
public class FutureResult<V> {

	private Object lock;

	private V result;

	private boolean done;

	public FutureResult(V initialValue) {
		lock = new Object();
		result = initialValue;
	}

	public boolean isDone() {
		return done;
	}

	public V get() throws InterruptedException, ExecutionException {
		synchronized (lock) {
			lock.wait();
			return result;
		}

	}

	public V get(long timeout) {
		synchronized (lock) {
			try {
				lock.wait(timeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

	public void setResult(V result) {
		synchronized (lock) {
			this.result = result;
			done = true;
			lock.notifyAll();
		}
	}

}
