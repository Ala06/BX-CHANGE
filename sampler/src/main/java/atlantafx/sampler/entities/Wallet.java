package atlantafx.sampler.entities;

import javafx.beans.property.*;

import java.util.Objects;

public class Wallet {
    private static int walletIds = 0;
    private int walletId;
    private double balance;
    private String currency;
    private int userId;

    public Wallet(int userId, double balance, String currency) {
        this.walletId= walletIds++;
        this.balance = balance;
        this.currency = currency;
        this.userId = userId;
    }
    public Wallet(){

    }

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance=balance;
    }



    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Wallet{"+
                " userId=" + userId +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return userId == wallet.userId &&
                Double.compare(wallet.balance, balance) == 0 &&
                Objects.equals(currency, wallet.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, userId, balance, currency);
    }
}
