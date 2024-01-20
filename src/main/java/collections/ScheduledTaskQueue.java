package collections;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.*;

public class ScheduledTaskQueue {
    public static void main(String[] args) {

        BlockingQueue<Task> queue = new DelayQueue<>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> producer.produce(new Task("Execute", scheduleAt12_00()))),
                CompletableFuture.runAsync(() -> System.out.println(consumer.consume()))
        ).join();
    }

    private static Duration scheduleAt12_00() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() >= 12) {
            currentTime = currentTime.plusHours(24);
        }
        Duration duration = Duration.between(currentTime, LocalTime.of(12, 0));
        System.out.println(duration.toHours() + " hours left");
        return duration;
    }

    private static class Task implements Delayed {

        private final String name;
        private Duration delay = Duration.ofNanos(0);

        private Task(String name) {
            this.name = name;
        }

        private Task(String name, Duration delay) {
            this.name = name;
            this.delay = delay;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(delay);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(delay.toMillis(), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "Task{" +
                    "name='" + name + '\'' +
                    ", delay=" + delay +
                    '}';
        }
    }

    private static class Producer {

        private final BlockingQueue<Task> queue;

        Producer(BlockingQueue<Task> queue) {
            this.queue = queue;
        }

        public void produce(Task element) {
            try {
                queue.put(element);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Consumer {

        private final BlockingQueue<Task> queue;

        Consumer(BlockingQueue<Task> queue) {
            this.queue = queue;
        }

        private Task consume() {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
