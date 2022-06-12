package com.example.threadlock.account;

import com.example.threadlock.infrastructure.redis.Control;
import com.example.threadlock.infrastructure.redis.Lock;
import com.example.threadlock.infrastructure.redis.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/account")
public class AccountWithdrawLockController{

    private final AccountRepository accountRepository;

    public AccountWithdrawLockController(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    //@RedisLock(keytype = Control.PATH_VARIABLE, key = "account",  locktype = Lock.TRYLOCK_WAITTIME_LEASETIME, waittime = 30000L, leasetime = 30000L)
    //@RedisLock(keytype = Control.KEY, key = "account", locktype = Lock.TRYLOCK, timeout = 5000L)
    @RedisLock(keytype = Control.KEY, key = "account", locktype = Lock.INTERRUPTIBLY)
    @GetMapping("/locked/{account}/withdraw/{amount}")
    public AccountResponseDto withdraw(@PathVariable String account, @PathVariable String amount) {

        Account accountEntity = accountRepository.getAccount(account);
        AccountResponseDto response = new AccountResponseDto();


//        try {
//            Thread.sleep(10000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        accountEntity.setAmount(accountEntity.getAmount() - Long.valueOf(amount));

        AccountDto accountDto = accountEntity.convertToDto(accountEntity);
        response.setData(accountDto);
        response.setResult("success");
        response.setMsg("withdraw success, amount " + accountEntity.getAmount());

        return response;
    }

}