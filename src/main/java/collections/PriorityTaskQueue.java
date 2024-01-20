package collections;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityTaskQueue {
    public static void main(String[] args) {

        BlockingQueue<Task> queue = new PriorityBlockingQueue<>(20,
                Comparator.comparing(Task::getName));
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> producer.produce(new Task("TaskB"))),
                CompletableFuture.runAsync(() -> producer.produce(new Task("TaskA"))),
                CompletableFuture.runAsync(() -> producer.produce(new Task("TaskC"))),
                CompletableFuture.runAsync(() -> System.out.println(consumer.consume()))
        ).join();
    }

    private static class Task {

        private final String name;

        private Task(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Producer {

        private final BlockingQueue<Task> queue;

        Producer(BlockingQueue<Task> queue) {
            this.queue = queue;
        }

        public void produce(Task task) {
            try {
                queue.put(task);
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
