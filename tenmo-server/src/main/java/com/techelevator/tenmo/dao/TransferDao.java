package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> getAllTransfers();

    public List<Transfer> getAllTransfersByAccountFrom(long accountFrom);

    public List<Transfer> getAllTransfersByAccountTo(long accountTo);

    public List<Transfer> getTransfersByAmount(BigDecimal amount);

    public Transfer getTransferById(long transferId);

    public Transfer sendBucks(Principal principal, long userTo, BigDecimal amountSent) throws AccountNotFoundException;

    public List<Transfer> getAllTransfersByType(long transferTypeId);

    public List<Transfer> getAllTransfersByStatus(long transferStatusId);


}
