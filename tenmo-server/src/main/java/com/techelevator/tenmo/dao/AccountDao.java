package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();

    Account findByUserId(long userId) throws AccountNotFoundException;

    BigDecimal getBalanceByUserId(long userId) throws AccountNotFoundException;

    BigDecimal addToBalance(BigDecimal amountAdded, long id) throws AccountNotFoundException;

    BigDecimal subtractFromBalance(BigDecimal amountSubtracted, long id) throws AccountNotFoundException;

   /* boolean update(Account account) throws AccountNotFoundException;*/

    /*Account create(Account account);

    void delete(Account account) throws AccountNotFoundException;*/
}
