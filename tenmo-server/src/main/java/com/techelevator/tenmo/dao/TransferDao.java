package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> getAllTransfers();
    public Transfer getTransferById(long transferId);
    public Transfer sendBucks(long userFrom, long userTo, BigDecimal amountSent);
}
