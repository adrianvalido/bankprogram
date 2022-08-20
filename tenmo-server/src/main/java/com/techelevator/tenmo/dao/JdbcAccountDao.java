package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

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
    public double getBalanceByUserId(long userId) throws AccountNotFoundException {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            mapRowToAccount(results).getBalance();
        }
        throw new AccountNotFoundException();
    }

    @Override
    public Account update(Account account) {
        String sql = "UPDATE account SET user_id = ?, balance = ? " +
                "WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, account.getUserId(), account.getBalance(), account.getAccountId());
        } catch (AccountNotFoundException e) {
            return null;
    }

    @Override
    public Account create(Account account) {
        return null;
    }

    @Override
    public void delete(Account account) {

    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(result.getLong("account_id"));
        account.setUserId(result.getLong("user_id"));
        account.setBalance(result.getDouble("balance"));
        return account;
    }
}
