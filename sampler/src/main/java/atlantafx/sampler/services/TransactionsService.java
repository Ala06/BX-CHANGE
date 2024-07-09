package atlantafx.sampler.services;

import atlantafx.sampler.entities.Transactions;
import atlantafx.sampler.entities.Wallet;

import java.util.List;

public interface TransactionsService {
    Transactions createTransaction(Transactions transaction);

    Transactions getTransactionById(int transactionId);

    List<Transactions> getAllTransactions();
    List<Transactions> getTransactionsByIds(List<Integer> transactionIds);

    Transactions updateTransaction(Transactions transaction);

    boolean deleteTransaction(int transactionsId);
}
