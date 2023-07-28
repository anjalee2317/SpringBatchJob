package com.example.springbatchjob.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "TRANSACTION")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
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

    public Transactions(String accountNumber, String transactionAmount, String description, String transactionDate, String transactionTime, String customerId) {
        this.accountNumber = accountNumber;
        this.transactionAmount = transactionAmount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.customerId = customerId;
    }

    public Transactions() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public OffsetDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(OffsetDateTime lastModified) {
        this.lastModified = lastModified;
    }

}