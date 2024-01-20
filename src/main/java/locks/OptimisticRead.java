package locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

@SuppressWarnings("Duplicates")
public class OptimisticRead {

    public static void main(String[] args) {

        Resource resource = new Resource();

        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            executorService.execute(() -> resource.write("value1"));
            executorService.execute(() -> resource.write("value2"));
            executorService.execute(() -> resource.write("value3"));
            executorService.execute(() -> System.out.println(resource.read()));
            executorService.execute(() -> System.out.println(resource.read()));
            executorService.execute(() -> System.out.println(resource.read()));
        }
    }

    private static class Resource {

        private String value;

        private final StampedLock lock = new StampedLock();

        public String read() {
            long stamp = lock.tryOptimisticRead();
            if (!lock.validate(stamp)) {
                long lockStamp = lock.readLock();
                try {
                    return "Switching to pessimistic lock: " + value;
                } finally {
                    lock.unlockRead(lockStamp);
                }
            } else return value;
        }

        public void write(String value) {
            long lockStamp = lock.writeLock();
            try {
                this.value = Thread.currentThread().getName() + ": " + value;
            } finally {
                lock.unlockWrite(lockStamp);
            }
        }
    }
}
