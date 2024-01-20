package spring;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    Account registerAccount(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Async
    void transfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        fromAccount.transfer(toAccount, amount);
        accounts.put(fromAccount.getId(), fromAccount);
        accounts.put(toAccount.getId(), toAccount);
    }

    @Async
    CompletableFuture<Account> getAccount(String accountId) {
        return CompletableFuture.supplyAsync(() -> accounts.get(accountId));
    }
}
