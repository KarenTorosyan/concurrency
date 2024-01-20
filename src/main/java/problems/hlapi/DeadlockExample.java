package problems.hlapi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExample {
    public static void main(String[] args) {

        Lock a = new ReentrantLock();
        Lock b = new ReentrantLock();

        new Thread(() -> {
            a.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\n\ta locked \n\tlocking b...");
                try {
                    b.lock();
                    System.out.println("\tb locked");
                } finally {
                    b.unlock();
                }
            } finally {
                a.unlock();
            }
        }).start();

        new Thread(() -> {
            b.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\n\tb locked \n\tlocking a...");
                try {
                    a.lock();
                    System.out.println("\ta locked");
                } finally {
                    a.unlock();
                }
            } finally {
                b.unlock();
            }
        }).start();
    }
}
