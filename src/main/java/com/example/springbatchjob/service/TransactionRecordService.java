package com.example.springbatchjob.service;

import com.example.springbatchjob.model.TransactionRecord;
import com.example.springbatchjob.repository.TransactionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRecordService {

    private final TransactionRecordRepository transactionRecordRepository;

    @Autowired
    public TransactionRecordService(TransactionRecordRepository transactionRecordRepository) {
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public List<TransactionRecord> getAllTransactions() {
        return transactionRecordRepository.findAll();
    }

    public TransactionRecord saveTransactions(TransactionRecord transactionRecord) {
        return transactionRecordRepository.save(transactionRecord);
    }

    public List<TransactionRecord> saveAllTransactions(List<TransactionRecord> transactions) {
        return transactionRecordRepository.saveAll(transactions);
    }

    public List<TransactionRecord> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRecordRepository.findByAccountNumber(accountNumber);
    }

    public List<TransactionRecord> getTransactionsByCustomerId(String customerId) {
        return transactionRecordRepository.findByCustomerId(customerId);
    }

    public Page<TransactionRecord> getTransactionRecordsByQuery(String customerId, List<String> accountNumbers,
                                                                String description, int page, int size) {

        return transactionRecordRepository.findByCustomerIdOrAccountNumberInOrDescriptionContaining(customerId,
                accountNumbers, description, PageRequest.of(page, size));
    }

    public void saveTransactionsByBatches(List<TransactionRecord> transactionRecordList) {
        transactionRecordRepository.saveAll(transactionRecordList);

    }
}
