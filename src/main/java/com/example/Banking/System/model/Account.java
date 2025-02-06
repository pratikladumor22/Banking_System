package com.example.Banking.System.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "account_details") // Specifies the table name in the database
public class Account {
    @Id
    @Column(name = "aNo") // Maps this field to the "aNo" column in the database
    private long accountNumber = g10dighit(); // Unique account number generated automatically

    @Column(name = "name") // Maps this field to the "name" column in the database
    private String Name; // Account holder's name

    @Column(name = "mobile_number") // Maps this field to the "mobile_number" column in the database
    private long mobileNumber; // Account holder's mobile number

    @Column(name = "balance")  // Maps this field to the "balance" column in the database
    private double balance; // Current balance in the account


    // Getter Setter Method

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Generates a unique 10-digit account number
    public static long g10dighit(){
        UUID num = UUID.randomUUID();
        long let = num.getLeastSignificantBits();
        long pos = Math.abs(let);
        return pos % 10000000000L;
    }
}
