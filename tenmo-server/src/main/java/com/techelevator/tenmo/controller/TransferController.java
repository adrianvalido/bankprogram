package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferDao dao;

    @RequestMapping(path = "/sendbucks", method = RequestMethod.POST)
    public Transfer sendBucksPost(@RequestBody Transfer transfer, Principal principal) throws AccountNotFoundException {

        dao.sendBucks(transfer, principal);
        return transfer;
    }

    @RequestMapping(path = "/alltransfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) throws AccountNotFoundException {
        return dao.getAllTransfers(principal);
    }

}
