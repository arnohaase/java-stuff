package de.arnohaase.javastuff.account_functional;

import com.ajjpj.afoundation.collection.immutable.AHashMap;
import com.ajjpj.afoundation.collection.immutable.AMap;
import com.ajjpj.afoundation.collection.immutable.AOption;

import java.util.concurrent.atomic.AtomicReference;


/**
 * @author arno
 */
public class Bank {
    private final AtomicReference<AMap<String, Account>> accounts = new AtomicReference<> (AHashMap.<String, Account> empty ());

    public void deposit (String id, int amount) {
        AMap<String, Account> prev, next;
        do {
            prev = accounts.get ();
            final AOption<Account> optAccount = prev.get (id);
            final Account account = optAccount.isDefined () ? optAccount.get ().modify (amount) : new Account (amount);
            next = prev.updated (id, account);
        }
        while (! accounts.compareAndSet (prev, next));
    }

    public int balance() {
        int result = 0;

        // the AMap is immutable, as are the Account objects --> this loop iterates over a snapshot ;-)
        for (Account a: accounts.get ().values ()) {
            result += a.getBalance ();
        }

        return result;
    }

    public void transfer (String from, String to, int amount) {
        if (from.equals (to)) { // Clou!
            return;
        }

        AMap<String, Account> prev, next;
        do {
            prev = accounts.get ();

            Account fromAccount = prev.getRequired (from);
            Account toAccount   = prev.getRequired (to);

            fromAccount = fromAccount.modify (-amount);
            toAccount   = toAccount  .modify (amount);

            next = prev
                    .updated (from, fromAccount)
                    .updated (to, toAccount);
        }
        while (! accounts.compareAndSet (prev, next));
    }
}
