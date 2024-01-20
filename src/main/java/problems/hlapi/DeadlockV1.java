package problems.hlapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("Duplicates")
public class DeadlockV1 {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 300);

        try(ExecutorService executorService = Executors.newFixedThreadPool(2)) {
            executorService.execute(() -> bobAccount.transfer(alisaAccount, 100));
            executorService.execute(() -> alisaAccount.transfer(bobAccount, 100));
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
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() +
                        "\n\tBefore transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);

                toAccount.retrieveAmount(this, amount);

                System.out.println("\tAfter transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);
            } finally {
                lock.unlock();
            }
        }

        private void retrieveAmount(Account fromAccount, int amount) {
            lock.lock();
            try {
                System.out.println("\tTransfer" +
                        "\n\t\t" + fromAccount.id + " transfer " + amount + " to " + id);
                fromAccount.balance -= amount;
                balance += amount;
            } finally {
                lock.unlock();
            }
        }
    }
}
