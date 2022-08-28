package com.techelevator.tenmo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class Transfer {
    @NotNull(message = "Account To field cannot be empty")
    private long transferId;
    @NotNull(message = "Account To field cannot be empty")
    private long transferTypeId;
    @NotNull(message = "Account To field cannot be empty")
    private long transferStatusId;
    @NotNull(message = "Account To field cannot be empty")
    private long accountFrom;
    @NotNull(message = "Account To field cannot be empty")
    private long accountTo;
    private BigDecimal amount;
    @NotNull(message = "UserIdFrom field cannot be empty")
    private long userIdFrom;
    @NotNull(message = "UserIdTo field cannot be empty")
    private long userIdTo;

    public Transfer(long transferId, long accountFrom, long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer(){
    }

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(long accountTo) { this.accountTo = accountTo; }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getTransferTypeId() { return transferTypeId; }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public long getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public long getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(long userIdTo) {
        this.userIdTo = userIdTo;
    }
}
