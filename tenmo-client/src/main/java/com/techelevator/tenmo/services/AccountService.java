package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controller.AccountController;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;

public class AccountService {

    private AccountController accountController;

    public BigDecimal viewCurrentBalance(Principal principal) throws AccountNotFoundException {
        return accountController.get(principal).getBalance();
    }


}
