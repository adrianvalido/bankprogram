package com.techelevator.tenmo.model;

import javax.validation.constraints.*;

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
    @DecimalMin(value = "0.01", message = "Amount field cannot be 0 or negative.")
    @NotNull(message = "Amount field cannot be null.")
    private double amount;

    public Transfer(long transferId, long accountFrom, long accountTo, double amount) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
}
