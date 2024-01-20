package locks;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class FairStarvationOn {

    public static void main(String[] args) {
        long timerStart = Instant.now().toEpochMilli();
        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            Object resource = new Object();
            for (int i = 0; i < 3; i++) {
                executorService.submit(new Task(resource));
            }
        }
        long timerEnd = Instant.now().toEpochMilli();
        System.out.println(timerEnd - timerStart + "ms");
    }

    static class Task implements Runnable {

        public static final int PROGRESS_TOTAL_PERCENT = 100;

        private final Object resource;

        public Task(Object resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            synchronized (resource) {
                for (int i = 0; i <= PROGRESS_TOTAL_PERCENT; i++) {
                    printProgress(i);
                    try {
                        resource.wait(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private static void printProgress(int percent) {
            double progress = (double) percent / PROGRESS_TOTAL_PERCENT * PROGRESS_TOTAL_PERCENT;
            if (progress % 20 == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + progress + "%");
            }
        }
    }
}
