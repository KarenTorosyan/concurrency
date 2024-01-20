package problems.hlapi;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LivelockExample {
    public static void main(String[] args) {

        Lock a = new ReentrantLock();
        Lock b = new ReentrantLock();

        new Thread(() -> {
            while (true) {
                a.lock();
                try {
                    while (!b.tryLock(1, TimeUnit.SECONDS)) {
                        System.out.println(Thread.currentThread().getName() + ": can't lock b");
                    }
                    b.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    a.unlock();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                b.lock();
                try {
                    while (!a.tryLock(1, TimeUnit.SECONDS)) {
                        System.out.println(Thread.currentThread().getName() + ": can't lock a");
                    }
                    a.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    b.unlock();
                }
            }
        }).start();
    }
}
