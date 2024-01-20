package problems.mutex;

@SuppressWarnings("Duplicates")
public class RaceConditionFix {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        Thread t1 = new Thread(counter::increment);
        Thread t2 = new Thread(counter::increment);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Counter is " + counter.num);
    }

    private static class Counter {

        private int num;

        public synchronized void increment() {
            for (int i = 0; i < 100000; i++) {
                num++;
            }
        }
    }
}
