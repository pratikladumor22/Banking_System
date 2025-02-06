package com.example.Banking.System.controller;

import com.example.Banking.System.DTO.*;
import com.example.Banking.System.model.Account;
import com.example.Banking.System.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountServices accountServices;

    // API to open a new account
    @PostMapping("open")
    public ResponceDTO open(@RequestBody Account account){
        return accountServices.openAccount(account);
    }

    // API to retrieve all accounts
    @GetMapping("getAllAccount")
    public List<Account> getall(){
        return accountServices.getAllAcoount();
    }

    // API to deposit money into an account
    @PostMapping("deposit")
    public ResponceDTO deposit(@RequestBody TransectionDTO transectionDTO){
        return  accountServices.Deposite(transectionDTO.getAccountNumber() , transectionDTO.getBalance());
    }

    // API to withdraw money from an account
    @PostMapping("withdraw")
    public ResponceDTO withdraw(@RequestBody TransectionDTO transectionDTO){
        return accountServices.withdraw(transectionDTO.getAccountNumber() , transectionDTO.getBalance());
    }

    // API to transfer money from one account to another
    @PutMapping("transfer")
    public ResponceDTO transfer(@RequestBody TransferDTO transferDTO){
        return accountServices.transfer(transferDTO.getFromAcNo() , transferDTO.getToAcNo() , transferDTO.getBalance());
    }

    // API to get Statment from an account
    @GetMapping("/getStatment")
    public List<StatmenetDTO> getstatment(@RequestBody StatementRequestDTO requestDTO){
        return accountServices.getStatment(requestDTO.getAccountNumber());
    }

    @DeleteMapping("/deleteAccount")
    public ResponceDTO deleteAccount(@RequestBody StatementRequestDTO requestDTO){
        return accountServices.deleteAccount(requestDTO.getAccountNumber());
    }
}
