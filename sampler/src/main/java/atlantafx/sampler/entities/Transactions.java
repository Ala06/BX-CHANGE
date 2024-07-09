package atlantafx.sampler.entities;

import java.sql.*;
import java.util.*;
import java.util.Objects;

public class Transactions {
//    private static int walletIds = 0;
    private int transactionId;
    private java.sql.Date transactionDate;
    private String type;
    private String currencyId;
    private double amount;
    private String source;

    public Transactions(int transactionId, java.sql.Date transactionDate, String type, String currencyId, double amount, String source) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.type = type;
        this.currencyId = currencyId;
        this.amount = amount;
        this.source = source;
    }

    public Transactions() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public java.sql.Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(java.sql.Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return transactionId == that.transactionId && Double.compare(amount, that.amount) == 0 && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(type, that.type) && Objects.equals(currencyId, that.currencyId) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, transactionDate, type, currencyId, amount, source);
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                ", type='" + type + '\'' +
                ", currencyId='" + currencyId + '\'' +
                ", amount=" + amount +
                ", source='" + source + '\'' +
                '}';
    }
}
