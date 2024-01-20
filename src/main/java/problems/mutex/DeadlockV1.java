package problems.mutex;

@SuppressWarnings("Duplicates")
public class DeadlockV1 {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 300);

        new Thread(() -> bobAccount.transfer(alisaAccount, 100)).start();
        new Thread(() -> alisaAccount.transfer(bobAccount, 100)).start();
    }

    private static class Account {

        private final String id;

        private int balance;

        public Account(String id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public synchronized void transfer(Account toAccount, int amount) {
            System.out.println(Thread.currentThread().getName() +
                    "\n\tBefore transfer" +
                    "\n\t\t" + id + " balance is " + balance +
                    "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);

            toAccount.retrieveAmount(this, amount);

            System.out.println("\tAfter transfer" +
                    "\n\t\t" + id + " balance is " + balance +
                    "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);
        }

        private synchronized void retrieveAmount(Account fromAccount, int amount) {
            System.out.println("\tTransfer" +
                    "\n\t\t" + fromAccount.id + " transfer " + amount + " to " + id);
            fromAccount.balance -= amount;
            balance += amount;
        }
    }
}
