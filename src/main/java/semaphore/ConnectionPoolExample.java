package semaphore;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolExample {
    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool(2);
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() ->
                        connectionPool.connect(ConnectionPoolExample::doWork)),
                CompletableFuture.runAsync(() ->
                        connectionPool.connect(ConnectionPoolExample::doWork)),
                CompletableFuture.runAsync(() ->
                        connectionPool.connect(ConnectionPoolExample::doWork))
        ).join();
    }

    private static void doWork() {
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + ": Connection successful");
        }
    }

    private static class ConnectionPool {

        private final Semaphore semaphore;

        public ConnectionPool(int maxParallelConnections) {
            this(maxParallelConnections, false);
        }

        public ConnectionPool(int maxParallelConnections, boolean fairQueue) {
            this.semaphore = new Semaphore(maxParallelConnections, fairQueue);
        }

        public void connect(Runnable runnable) {
            try {
                while (!semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + ": Retrying...");
                }
                try {
                    runnable.run();
                } finally {
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
