package com.example.springbatchjob.controller;

import com.example.springbatchjob.model.TransactionRecord;
import com.example.springbatchjob.repository.TransactionRecordRepository;
import com.example.springbatchjob.service.TransactionRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class TransactionRecordController {

    private final int BATCH_SIZE = 20;
    private final TransactionRecordRepository transactionRecordRepository;
    private final TransactionRecordService transactionRecordService;
    private final ExecutorService executorService;

    public TransactionRecordController(TransactionRecordRepository transactionRecordRepository,
                                       TransactionRecordService transactionRecordService) {
        this.transactionRecordRepository = transactionRecordRepository;
        this.transactionRecordService = transactionRecordService;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @PostMapping("/save")
    private void consumeTransactionRecords(@RequestBody String filePath) {

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            List<TransactionRecord> transactions = lines
                    .skip(1) // Skip the first row (header) of the file
                    .map(line -> Arrays.asList(line.split("\\|")))
                    .map(data -> TransactionRecord.builder()
                            .accountNumber(Long.getLong(data.get(0)))
                            .transactionAmount(data.get(1))
                            .description(data.get(2))
                            .transactionDate(data.get(3))
                            .transactionTime(data.get(4))
                            .customerId(data.get(5))
                            .build())
                    .toList();

            List<TransactionRecord> batchList = new ArrayList<>();
            for (TransactionRecord transaction : transactions) {
                batchList.add(transaction);
                if (batchList.size() >= BATCH_SIZE) {
                    transactionRecordService.saveTransactionsByBatches(batchList);
                    batchList.clear();
                }
            }

            if (!batchList.isEmpty()) {
                transactionRecordService.saveTransactionsByBatches(batchList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/data")
    private List<TransactionRecord> getFileData() {
        return transactionRecordRepository.findAll();
    }

    @GetMapping("/page-data")
    private Page<TransactionRecord> getPageableData(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRecordRepository.findAll(pageable);
    }

    @GetMapping("/retrieve")
    public ResponseEntity<Page<TransactionRecord>> getTransactionRecords(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) List<String> accountNumbers,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Fetch records based on the given parameters
        Page<TransactionRecord> records = transactionRecordService.getTransactionRecordsByQuery(customerId,
                accountNumbers, description, page, size);

        if (records.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(records, HttpStatus.OK);
        }
    }


    @Async
    @PatchMapping("/update")
    public CompletableFuture<Void> updateTransactionRecords(@RequestBody List<TransactionRecord> updatedDataRecords) {
        for (TransactionRecord updatedRecord : updatedDataRecords) {
            processTransactionRecord(updatedRecord);
        }
        return CompletableFuture.completedFuture(null);
    }

    private void processTransactionRecord(TransactionRecord updatedRecord) {
        TransactionRecord existingTransaction = transactionRecordRepository.findById(updatedRecord.getId())
                .orElse(null);

        if (existingTransaction != null) {
            existingTransaction.setDescription(updatedRecord.getDescription());
            transactionRecordService.saveTransactions(existingTransaction);
        } else {
            transactionRecordService.saveTransactions(updatedRecord);
        }
    }

    @PatchMapping("/update-data")
    public void updateData(@RequestBody List<TransactionRecord> updatedDataRecords) {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        for (TransactionRecord updatedRecord : updatedDataRecords) {
            threadPool.submit(() -> {
                TransactionRecord existingTransaction = Optional.ofNullable(updatedRecord.getId())
                        .map(transactionRecordRepository::getReferenceById)
                        .orElse(null);

                if (existingTransaction != null) {
                    existingTransaction.setDescription(updatedRecord.getDescription());
                    transactionRecordService.saveTransactions(existingTransaction);
                } else {
                    transactionRecordService.saveTransactions(updatedRecord);
                }
            });
        }
        threadPool.shutdown();
    }
}