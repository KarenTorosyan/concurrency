package problems.hlapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Best practices to resolve
 * + Memory inconsistency
 * + Deadlock
 * */

@SuppressWarnings("Duplicates")
public class DeadlockFix {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 200);

        /*
         * Bad practice
         * - Memory inconsistency possible
         * for (int i = 0; i < 1000; i++) {
         * new Thread(() -> bobAccount.transfer(alisaAccount, 100)).start();
         * new Thread(() -> alisaAccount.transfer(bobAccount, 100)).start();
         * }
         */

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

        private final Lock lock = new ReentrantLock();

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
            Account firstAccount = id.compareTo(toAccount.id) < 0 ? this : toAccount;
            Account secondAccount = firstAccount == this ? toAccount : this;
            firstAccount.lock.lock();
            secondAccount.lock.lock();
            try {
                runnable.run();
            } finally {
                firstAccount.lock.unlock();
                secondAccount.lock.unlock();
            }
        }
    }
}
