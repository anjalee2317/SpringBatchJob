package com.example.springbatchjob.repository;


import com.example.springbatchjob.model.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
    @Query("SELECT t FROM TransactionRecord t WHERE t.accountNumber = :accountNumber")
    List<TransactionRecord> findByAccountNumber(String accountNumber);


    @Query("SELECT t FROM TransactionRecord t WHERE t.customerId = :customerId")
    List<TransactionRecord> findByCustomerId(String customerId);

    @Query(value = "SELECT CURRENT_TIMESTAMP", nativeQuery = true)
    OffsetDateTime getCurrentTimestamp();

    @Query("SELECT t FROM TransactionRecord t WHERE (:customerId IS NULL OR t.customerId = :customerId) " +
            "AND (:accountNumbers IS NULL OR t.accountNumber IN :accountNumbers) " +
            "AND (:description IS NULL OR t.description LIKE %:description%)")
    Page<TransactionRecord> findByCustomerIdOrAccountNumberInOrDescriptionContaining(
            @Param("customerId") String customerId,
            @Param("accountNumbers") List<String> accountNumbers,
            @Param("description") String description,
            Pageable pageable);
}

