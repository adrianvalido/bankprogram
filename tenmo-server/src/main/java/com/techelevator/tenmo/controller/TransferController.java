package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;


@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferDao dao;

    @RequestMapping(path = "/sendbucks", method = RequestMethod.POST)
    public Transfer createTransfer(Principal principal, BigDecimal amount, long accountTo) throws AccountNotFoundException {
        Transfer transfer = dao.createTransfer(principal, amount, accountTo);
        dao.sendBucks(transfer);
        return transfer;
    }

}
