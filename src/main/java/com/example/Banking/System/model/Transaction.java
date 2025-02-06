package com.example.Banking.System.model;

import com.example.Banking.System.repository.AccountRepository;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_details") // Specifies the table name in the database
public class Transaction {
    @Id
    private long id; // Unique identifier for the transaction

    @ManyToOne(cascade = CascadeType.ALL) // Many transactions can be associated with one account
    private Account account; // The account associated with this transaction

    private String type; // Type of transaction (e.g., deposit, withdrawal)

    private double Balance; // Amount involved in the transaction

    private LocalDateTime date; // Date and time when the transaction occurred

    // Getter Setter Method


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
