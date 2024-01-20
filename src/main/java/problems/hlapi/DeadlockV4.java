package problems.hlapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("Duplicates")
public class DeadlockV4 {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 300);

        for (int i = 0; i < 100; i++) {
            try (ExecutorService executorService = Executors.newFixedThreadPool(2)) {
                executorService.execute(() -> bobAccount.transfer(alisaAccount, 100));
                executorService.execute(() -> alisaAccount.transfer(bobAccount, 100));
            }
        }
    }

    private static class Account {

        private final String id;

        private int balance;

        private final ReentrantLock lock = new ReentrantLock();
        private final Condition accountLocked = lock.newCondition();

        public Account(String id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public void transfer(Account toAccount, int amount) {
            lockAccounts(toAccount, () -> {
                System.out.println(Thread.currentThread().getName() +
                        "\n\tBefore transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);

                System.out.println("\tTransfer" +
                        "\n\t\t" + id + " transfer " + amount + " to " + toAccount.id);

                balance -= amount;
                toAccount.balance += amount;

                System.out.println("\tAfter transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);
            });
        }

        private void lockAccounts(Account toAccount, Runnable runnable) {
            lock.lock();
            try {
                while (!toAccount.lock.tryLock()) {
                    System.out.println(Thread.currentThread().getName() +
                            "\n\t " + id + " account locked by another thread, waiting...");
                    try {
                        accountLocked.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                runnable.run();
                accountLocked.signal();
            } finally {
                lock.unlock();
                toAccount.lock.unlock();
            }
        }
    }
}
