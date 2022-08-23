package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account findByUserId(long userId) throws AccountNotFoundException {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            return mapRowToAccount(results);
        }
        throw new AccountNotFoundException();
    }

    @Override
    public BigDecimal getBalanceByUserId(long userId) throws AccountNotFoundException {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            return mapRowToAccount(results).getBalance();
        }
        throw new AccountNotFoundException();
    }

    @Override
    public boolean update(Account account) throws AccountNotFoundException {
        String sql = "UPDATE account SET user_id = ?, balance = ? " +
                "WHERE account_id = ?;";
        boolean isSuccessful = false;
            int status = jdbcTemplate.update(sql, account.getUserId(), account.getBalance(), account.getAccountId());
            if (status == 1) {
                isSuccessful = true;
            } else {
                throw new AccountNotFoundException();
            }
        return isSuccessful;
    }


/*    @Override
    public Account create(Account account) {
        return null;
    }

    @Override
    public void delete(Account account) throws AccountNotFoundException {

    }*/

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
