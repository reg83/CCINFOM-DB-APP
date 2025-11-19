package com.countryclub.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {

    private int transactionId;
    private Integer memberId; 
    private Integer eventId; 
    private Date transactionDate; 
    private BigDecimal amount; 
    private String description;
    private String transactionType; 

    public Transaction() {
    }

    public Transaction(Integer memberId, Date transactionDate, BigDecimal amount, String transactionType, String description) {
        this(memberId, null, transactionDate, amount, transactionType, description);
    }

    public Transaction(Integer memberId, Integer eventId, Date transactionDate, BigDecimal amount, String transactionType, String description) {
        this.memberId = memberId;
        this.eventId = eventId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
    }

    public Transaction(int transactionId, Integer memberId, Integer eventId, Date transactionDate, BigDecimal amount, String description) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.eventId = eventId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public Integer getEventId() { return eventId; } 
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}