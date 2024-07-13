package info.jab.ms.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

public class CustomFuture<V> implements Future<V> {
    private final Object lock = new Object();
    private boolean isDone = false;
    private V result = null;
    private Throwable exception = null;

    public CustomFuture(Callable<V> task) {
        new Thread(() -> {
            try {
                V res = task.call();
                synchronized (lock) {
                    result = res;
                    isDone = true;
                    lock.notifyAll();
                }
            } catch (Exception e) {
                synchronized (lock) {
                    exception = e;
                    isDone = true;
                    lock.notifyAll();
                }
            }
        }).start();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // Not supporting cancellation in this custom implementation
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        synchronized (lock) {
            return isDone;
        }
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while (!isDone) {
                lock.wait();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!isDone) {
                lock.wait(unit.toMillis(timeout));
            }
            if (!isDone) {
                throw new TimeoutException("Timeout expired");
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        }
    }
}
