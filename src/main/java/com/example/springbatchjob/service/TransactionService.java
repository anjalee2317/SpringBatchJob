package com.example.springbatchjob.service;

import com.example.springbatchjob.model.Transactions;
import com.example.springbatchjob.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transactions saveTransactions(Transactions transactions) {
        return transactionRepository.save(transactions);
    }

    public List<Transactions> saveAllTransactions(List<Transactions> transactions) {
        return transactionRepository.saveAll(transactions);
    }

    public List<Transactions> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public List<Transactions> getTransactionsByCustomerId(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

}
