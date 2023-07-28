package com.example.springbatchjob.repository;


import com.example.springbatchjob.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT t FROM Transactions t WHERE t.accountNumber = :accountNumber")
    List<Transactions> findByAccountNumber(String accountNumber);


    @Query("SELECT t FROM Transactions t WHERE t.customerId = :customerId")
    List<Transactions> findByCustomerId(String customerId);

    @Query(value = "SELECT CURRENT_TIMESTAMP", nativeQuery = true)
    OffsetDateTime getCurrentTimestamp();

}

