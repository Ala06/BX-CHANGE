package atlantafx.sampler.entities;

import java.util.Objects;

public class TransactionList {

    private int walletId;
    private int transactionId;

    public TransactionList(int walletId, int transactionId) {
        this.walletId = walletId;
        this.transactionId = transactionId;
    }

    public TransactionList() {
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TransactionList{" +
                "walletId=" + walletId +
                ", transactionId=" + transactionId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionList that = (TransactionList) o;
        return walletId == that.walletId && transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, transactionId);
    }
}
