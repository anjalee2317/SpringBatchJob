package com.example.springbatchjob.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRANSACTION_RECORD")
public class TransactionRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCOUNT_NUMBER")
    private Long accountNumber;

    @Column(name = "TRX_AMOUNT")
    private String transactionAmount;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TRX_DATE")
    private String transactionDate;

    @Column(name = "TRX_TIME")
    private String transactionTime;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Version
    private OffsetDateTime lastModified;

}