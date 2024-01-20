package locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("Duplicates")
public class PessimisticRead {

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

        private final Lock lock = new ReentrantLock();

        public String read() {
            lock.lock();
            try {
                return value;
            } finally {
                lock.unlock();
            }
        }

        public void write(String value) {
            lock.lock();
            try {
                this.value = Thread.currentThread().getName() + ": " + value;
            } finally {
                lock.unlock();
            }
        }
    }
}
