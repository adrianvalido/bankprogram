package com.techelevator.tenmo.model;

public class Account {

    private long accountId;
    private long userId;
    private double balance;

    public Account() {}

    public Account(long userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getUserId() { return userId; }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
