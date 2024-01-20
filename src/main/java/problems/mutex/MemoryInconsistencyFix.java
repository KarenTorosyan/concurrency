package problems.mutex;

@SuppressWarnings("Duplicates")
public class MemoryInconsistencyFix {
    public static void main(String[] args) {
        Account bobAccount = new Account("Bob", 200);
        Account alisaAccount = new Account("Alisa", 200);
        Account annaAccount = new Account("Anna", 200);

        new Thread(() -> bobAccount.transfer(alisaAccount, 200)).start();
        new Thread(() -> alisaAccount.transfer(annaAccount, 300)).start();
    }

    private static class Account {

        private final String id;

        private int balance;

        public Account(String id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public synchronized void transfer(Account toAccount, int amount) {
            synchronized (toAccount) {
                if (balance < amount) {
                    System.out.println(Thread.currentThread().getName() +
                            "\n\t" + id + " wants transfer " + amount + " to " +
                            toAccount.id + ", but balance is " + balance);
                    return;
                }

                System.out.println(Thread.currentThread().getName() +
                        "\n\t Before transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);

                System.out.println("\tTransfer" +
                        "\n\t\t" + id + " transfer " + amount + " to " + toAccount.id);
                balance -= amount;
                toAccount.balance += amount;

                System.out.println("\tAfter transfer" +
                        "\n\t\t" + id + " balance is " + balance +
                        "\n\t\t" + toAccount.id + " balance is " + toAccount.balance);
            }
        }
    }
}
