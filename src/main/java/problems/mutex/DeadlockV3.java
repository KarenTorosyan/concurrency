package problems.mutex;

@SuppressWarnings("Duplicates")
public class DeadlockV3 {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 300);

        for (int i = 0; i < 2; i++) {
            new Thread(() -> bobAccount.transfer(alisaAccount, 100)).start();
            new Thread(() -> alisaAccount.transfer(bobAccount, 100)).start();
        }
    }

    private static class Account {

        private final String id;

        private int balance;

        public Account(String id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public synchronized void transfer(Account toAccount, int amount) {
            toAccount.retrieveAmount(this, amount);
        }

        private synchronized void retrieveAmount(Account fromAccount, int amount) {
            System.out.println(Thread.currentThread().getName() + "\n\tBefore transfer" +
                    "\n\t\t" + fromAccount.id + " balance is " + fromAccount.balance +
                    "\n\t\t" + id + " balance is " + balance);

            System.out.println("\tTransfer" +
                    "\n\t\t" + fromAccount.id + " transfer " + amount + " to " + id);
            fromAccount.balance -= amount;
            balance += amount;

            System.out.println("\tAfter transfer" +
                    "\n\t\t" + fromAccount.id + " balance is " + fromAccount.balance +
                    "\n\t\t" + id + " balance is " + balance);
        }
    }
}
