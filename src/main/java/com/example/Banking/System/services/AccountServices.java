package com.example.Banking.System.services;

import com.example.Banking.System.DTO.ResponceDTO;
import com.example.Banking.System.DTO.StatmenetDTO;
import com.example.Banking.System.model.Account;
import com.example.Banking.System.model.Transaction;
import com.example.Banking.System.model.Types;
import com.example.Banking.System.repository.AccountRepository;
import com.example.Banking.System.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServices {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    smsServices services;


    // Account Open Services
    public ResponceDTO openAccount(Account account){
        ResponceDTO responceDTO = new ResponceDTO();
        try {
            // Validate the account object
            if (account == null) {
                responceDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                responceDTO.setMessage("Account details cannot be null");
                return responceDTO;
            }

            // Save the account
            accountRepository.save(account);

            // Construct SMS and WhatsAPP message
            String smsNumber = "+91" + String.valueOf(account.getMobileNumber());
            String smsMessage  = String.format(
                    "Dear %s, your account number (%s) has been successfully opened with HDBC Bank.  Available Balance: ₹%.2f ,  Welcome aboard! For assistance, call (+21784569).",
                    account.getName(), account.getAccountNumber(), account.getBalance()
            );

            // send SMS and WhatsApp Notification
            services.sendSms(smsNumber,smsMessage);
            services.whmessge(smsNumber,smsMessage);

            responceDTO.setHttpStatus(HttpStatus.CREATED);
            responceDTO.setMessage("New account is created successfully");
        }catch (Exception e){
            responceDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responceDTO.setMessage("An error occurred: " + e.getMessage());
        }
        return responceDTO;
    }

    // Account GetALL Services
    public List<Account> getAllAcoount(){
       return accountRepository.findAll();
    }

    // Balance Deposite Services
    public ResponceDTO Deposite(long accountNumber , double balance) {
        ResponceDTO  responceDTO = new ResponceDTO();
        try {
            Optional<Account> account = accountRepository.findById(accountNumber);

            // Check if the account exists
            if (account.isEmpty()) {
                responceDTO.setHttpStatus(HttpStatus.NOT_FOUND);
                responceDTO.setMessage("Account not found");
                return responceDTO;
            }

            // Validate the balance
            if (balance <= 0) {
                responceDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                responceDTO.setMessage("Deposit amount must be greater than zero");
                return responceDTO;
            }
            account.get().setBalance(account.get().getBalance() + balance);

            // Record the transaction
            TransectionRecord(account.get(),Types.Diposit,balance);

            // Save the updated account
            accountRepository.save(account.get());

            // Construct SMS and WhatsAPP message
            String smsNumber = "+91" + String.valueOf(account.get().getMobileNumber());
            String smsMessage = String.format(
                    "Dear %s, a deposit of ₹%.2f has been credited to your account (%s). Available Balance: ₹%.2f. Thank you for banking with HDBC Bank.",
                    account.get().getName(), balance, account.get().getAccountNumber(), account.get().getBalance()
            );

            // send SMS and WhatsApp Notification
            services.sendSms(smsNumber,smsMessage);
            services.whmessge(smsNumber,smsMessage);

            responceDTO.setMessage("Deposite successful");
            responceDTO.setHttpStatus(HttpStatus.OK);
        }catch (Exception e){
            responceDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responceDTO.setMessage(e.getMessage());
        }
        return responceDTO;
    }

    // Withdraw Balance Services
    public ResponceDTO withdraw(long accountNumber , double balance){
        ResponceDTO responceDTO = new ResponceDTO();
        try {
            Optional<Account> account = accountRepository.findById(accountNumber);

            // Check if the account exists
            if (account.isEmpty()) {
                responceDTO.setHttpStatus(HttpStatus.NOT_FOUND);
                responceDTO.setMessage("Account not found");
                return responceDTO;
            }

            // Check for sufficient balance
            if (account.get().getBalance() < balance) {
                responceDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                responceDTO.setMessage("Insufficient balance");
                return responceDTO;
            }

            // Perform the withdrawal
            account.get().setBalance(account.get().getBalance() - balance);

            // Record the transaction
            TransectionRecord(account.get(),Types.Withdraw,balance);

            // Save the updated account
            accountRepository.save(account.get());

            // Construct SMS and WhatsAPP message
            String smsNumber = "+91" + String.valueOf(account.get().getMobileNumber());
            String smsMessage = String.format(
                    "Dear %s, a withdrawal of ₹%.2f has been debited from your account (%s). Available Balance: ₹%.2f. If this was not you, contact (+21784569) immediately.",
                    account.get().getName(), balance, account.get().getAccountNumber(), account.get().getBalance()
            );

            // send SMS and WhatsApp Notification
            services.sendSms(smsNumber,smsMessage);
            services.whmessge(smsNumber,smsMessage);

            responceDTO.setMessage("withdraw successful");
            responceDTO.setHttpStatus(HttpStatus.OK);
        }catch (Exception e){
            responceDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responceDTO.setMessage(e.getMessage());
        }
        return responceDTO;
    }

    // Transecation id Auto Genrate Method
    public long autoG(){
        UUID id = UUID.randomUUID();
        long num = Math.abs(id.getMostSignificantBits());
        while (String.valueOf(num).length() < 10){
            num *= 10;
        }
        return num % 1_000_000_0000L;
    }

    // TransectionRecord Services
    public void TransectionRecord(Account account , Types types , double balance){
        if (account == null || types == null || balance <= 0) {
            throw new IllegalArgumentException("Invalid account, type, or balance");
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setId(autoG()); // Ensure autoG() generates a unique ID
        transaction.setBalance(balance);
        transaction.setType(String.valueOf(types));
        transaction.setDate(LocalDateTime.now());
        try {
            transactionRepository.save(transaction);
        }catch (Exception e){
            // Log the error (consider using a logging framework)
            System.err.println("Error saving transaction: " + e.getMessage());
            // Optionally, rethrow the exception or handle it as needed
        }
    }


    // Transfer Services
    public ResponceDTO transfer(long fromAcNo , long ToAcNo , double balance){
        ResponceDTO responseDTO = new ResponceDTO();
        try {
            Optional<Account> from = accountRepository.findById(fromAcNo);
            Optional<Account> to = accountRepository.findById(ToAcNo);

            if (from.isEmpty()) {
                responseDTO.setHttpStatus(HttpStatus.NOT_FOUND);
                responseDTO.setMessage("From account not found");
                return responseDTO;
            }

            if (to.isEmpty()) {
                responseDTO.setHttpStatus(HttpStatus.NOT_FOUND);
                responseDTO.setMessage("To account not found");
                return responseDTO;
            }

            if (from.get().getBalance() < balance) {
                responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseDTO.setMessage("Insufficient balance");
                return responseDTO;
            }

            // Perform the transfer
            from.get().setBalance(from.get().getBalance() - balance);
            to.get().setBalance(to.get().getBalance() + balance);

            accountRepository.save(from.get());
            accountRepository.save(to.get());

            String smsNumber = "+91" + String.valueOf(from.get().getMobileNumber());
            String smsMessage = String.format(
                    "Dear %s, a transfer of ₹%.2f has been made from your account (%s) to account (%s). " +
                            "Your new balance is ₹%.2f. If this was not you, please contact us immediately at (+21784569).",
                    from.get().getName(), // Sender's name
                    balance,               // Transfer amount
                    from.get().getAccountNumber(), // Sender's account number
                    to.get().getAccountNumber(),   // Receiver's account number
                    from.get().getBalance()         // Updated balance after the transfer
            );

            services.sendSms(smsNumber,smsMessage);
            services.whmessge(smsNumber,smsMessage);

            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setMessage("Account Transfer Successfully");
        } catch (Exception e) {
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public List<StatmenetDTO> getStatment(long accountNumber){
        // Fetch all transactions from the repository
        List<Transaction> transactions = transactionRepository.findAll();

        // Create a list to hold the statement DTOs
        List<StatmenetDTO> statmenetDTOS = new ArrayList<>();

        // Iterate through each transaction to filter by account number
        for (int i = 0 ; i < transactions.size();i++){
            Transaction y = transactions.get(i);  // Get the current transaction
            Account t = y.getAccount(); // Get the associated account

            // Check if the transaction belongs to the specified account number
            if (t.getAccountNumber() != accountNumber){
                continue; // Skip to the next transaction if the account number does not match
            }

            // Create a new StatementDTO to hold transaction details
            StatmenetDTO statmenetDTO = new StatmenetDTO();
            statmenetDTO.setAccountNumber(t.getAccountNumber()); // Set the account number
            statmenetDTO.setBalance(y.getBalance()); // Set the transaction balance
            statmenetDTO.setDate(y.getDate()); // Set the transaction date
            statmenetDTO.setType(y.getType()); // Set the transaction type

            // Add the populated StatementDTO to the list
            statmenetDTOS.add(statmenetDTO);
        }
        // Return the list of StatementDTOs for the specified account number
        return statmenetDTOS;
    }

    public ResponceDTO deleteAccount(long accountNumber){
        ResponceDTO responceDTO = new ResponceDTO();
        try {
            // Retrieve the account from the repository
            Optional<Account> accountOptional = accountRepository.findById(accountNumber);

            // Check if the account exists
            if (accountOptional.isEmpty()){
                responceDTO.setHttpStatus(HttpStatus.NOT_FOUND);
                responceDTO.setMessage("Account Not Found");
                return responceDTO;
            }
            Account account = accountOptional.get();

            transactionRepository.deleteByAccount(account);

            // Delete the account
            accountRepository.delete(account);

            // Construct SMS/WhatsApp notification
            String smsNumber = "+91" + String.valueOf(account.getMobileNumber());;
            String smsMessage = String.format(
                    "Dear %s, your account (%s) has been successfully closed. If this was not requested by you, contact our support immediately.",
                    account.getName(), account.getAccountNumber()
            );

            // Send SMS and WhatsApp notifications
            services.sendSms(smsNumber, smsMessage);
            services.whmessge(smsNumber, smsMessage);
            responceDTO.setHttpStatus(HttpStatus.OK);
            responceDTO.setMessage("Account deleted successfully");
        }catch (Exception e){
            responceDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responceDTO.setMessage("Error occurred while deleting the account:" +e.getMessage());
        }
        return responceDTO;
    }

}
