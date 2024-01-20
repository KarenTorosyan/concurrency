package problems.hlapi;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RaceConditionFix {
    public static void main(String[] args) {

        Counter counter = new Counter();

        CompletableFuture.allOf(
                CompletableFuture.runAsync(counter::increment),
                CompletableFuture.runAsync(counter::increment)
        ).join();

        System.out.println("Counter is " + counter.num);
    }

    private static class Counter {

        private int num;

        private final Lock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                for (int i = 0; i < 100000; i++) {
                    num++;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
