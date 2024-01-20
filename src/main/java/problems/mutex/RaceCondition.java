package problems.mutex;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("Duplicates")
public class RaceCondition {
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

        public void increment() {
            for (int i = 0; i < 100000; i++) {
                num++;
            }
        }
    }
}
