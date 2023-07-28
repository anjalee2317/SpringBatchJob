package com.example.springbatchjob.controller;

import com.example.springbatchjob.model.Transactions;
import com.example.springbatchjob.repository.TransactionRepository;
import com.example.springbatchjob.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    private final TransactionService transactionService;
    private String filePath;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    public DataController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/path")
    public void setFilePath(@RequestBody String filePath) {
        this.filePath = filePath;
    }

    @PostMapping("/save")
    private void saveFileData() {

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                List<String> data = Arrays.asList(line.split("\\|"));
                String accountNumber = data.get(0);
                String transactionAmount = data.get(1);
                String description = data.get(2);
                String transactionDate = data.get(3);
                String transactionTime = data.get(4);
                String customerId = data.get(5);

                transactionService.saveTransactions(new Transactions(accountNumber, transactionAmount, description, transactionDate, transactionTime, customerId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/data")
    private List<Transactions> getFileData() {
        return transactionRepository.findAll();
    }

    @GetMapping("/page-data")
    private Page<Transactions> getPageableData(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable);
    }

    @PatchMapping("/update")
    public void updateData(@RequestBody List<Transactions> updatedData) {
        for (Transactions updatedRow : updatedData) {
            List<Transactions> existingTransactions = transactionService.getTransactionsByAccountNumber(updatedRow.getAccountNumber());

            if (!(existingTransactions.size() == 0)) {
                for (Transactions existingTransaction : existingTransactions) {
                    OffsetDateTime currentTimestamp = transactionRepository.getCurrentTimestamp();
                    if (!existingTransaction.getLastModified().equals(currentTimestamp)) {
                        if (updatedRow.getDescription() != null) {
                            existingTransaction.setDescription(updatedRow.getDescription());
                        }
                        transactionService.saveTransactions(existingTransaction);
                    } else {
                        // Handle conflict: The row has been updated by another user, reject the update
                        // You can throw an exception or send an error response to the client
                    }
                }
            } else {
                transactionService.saveTransactions(updatedRow);
            }
        }
    }
}