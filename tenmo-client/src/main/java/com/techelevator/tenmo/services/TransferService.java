package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class TransferService {

    private TransferController transferController;

    public List<Transfer> getAllTransfers(Principal principal) throws AccountNotFoundException {
        return transferController.getAllTransfers(principal);
    }

    public Transfer sendBucks(Transfer transfer, Principal principal) throws AccountNotFoundException{
        return transferController.sendBucksPost(transfer,principal);
    }

    public Transfer createTransfer(Principal principal, BigDecimal amount, long accountTo)throws AccountNotFoundException{
        return transferController.createTransfer(principal,amount,accountTo);
    }

    public Transfer getTransferById(long transferId){
        return transferController.getTransferById(transferId);
    }

}
