package atlantafx.sampler.services;

import atlantafx.sampler.entities.TransactionList;

import java.util.List;

public interface TransactionListService {

    void addTransactionToWallet(int transactionId, int walletId);

    List<Integer> getTransactionsByWalletId(int walletId);

    boolean removeTransactionFromWallet(int transactionId, int walletId);

}
