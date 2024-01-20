package spring;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("Duplicates")
public class Account {

    private final String id;

    private BigDecimal balance;

    private final Lock lock = new ReentrantLock();

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void transfer(Account toAccount, BigDecimal amount) {
        lockAccounts(toAccount, () -> {
            System.out.println(Thread.currentThread().getName() +
                    "\n\tBefore transfer" +
                    "\n\t\t" + id + " balance is " + balance +
                    "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);

            System.out.println("\tTransfer" +
                    "\n\t\t" + id + " transfer " + amount + " to " + toAccount.id);
            balance = balance.subtract(amount);
            toAccount.balance = toAccount.balance.add(amount);

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
