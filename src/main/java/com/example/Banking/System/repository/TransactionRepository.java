package com.example.Banking.System.repository;

import com.example.Banking.System.model.Account;
import com.example.Banking.System.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Long> {

    @Transactional
    void deleteByAccount(Account account);
}
