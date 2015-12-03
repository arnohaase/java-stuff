package de.arnohaase.javastuff.account_functional;

/**
 * @author arno
 */
public class Account {
    private final int balance;

    public Account (int balance) {
        this.balance = balance;
    }

    public int getBalance () {
        return balance;
    }

    public Account modify (int diff) {
        if (balance + diff < 0) {
            throw new IllegalArgumentException ();
        }
        return new Account (balance + diff);
    }

    @Override public String toString () {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}
