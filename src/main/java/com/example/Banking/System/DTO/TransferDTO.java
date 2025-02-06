package com.example.Banking.System.DTO;

public class TransferDTO {
    private long fromAcNo;
    private long ToAcNo;
    private double balance;

    public long getFromAcNo() {
        return fromAcNo;
    }

    public void setFromAcNo(long fromAcNo) {
        this.fromAcNo = fromAcNo;
    }

    public long getToAcNo() {
        return ToAcNo;
    }

    public void setToAcNo(long toAcNo) {
        ToAcNo = toAcNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
