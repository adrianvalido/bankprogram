package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private JdbcTemplate jdbcTemplate;


    private AccountDao dao;

    @RequestMapping(path = "/balance/{userId}", method = RequestMethod.GET)
    public BigDecimal get(@PathVariable long userId) throws AccountNotFoundException {
        /*if (dao.findByUserId(userId).getAccountId() > 0) {
            return dao.getBalanceByUserId(userId);
        } else {
            throw new AccountNotFoundException();
        }*/

        String sql = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (Exception e) {
            throw new AccountNotFoundException();
        }
        return balance;
    }



}
