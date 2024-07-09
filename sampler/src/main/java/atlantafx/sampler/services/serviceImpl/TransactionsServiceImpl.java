package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Transactions;
import atlantafx.sampler.services.TransactionsService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionsServiceImpl implements TransactionsService {

    private static final Logger logger = Logger.getLogger(TransactionsService.class.getName());

    @Override
    public Transactions createTransaction(Transactions transaction) {
        String sql = "INSERT INTO transactions (date, type, amount, Currency_id, source) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDate(1, transaction.getTransactionDate());
            preparedStatement.setString(2, transaction.getType());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getCurrencyId());
            preparedStatement.setString(5, transaction.getSource());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Transaction created successfully");
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating transaction", e);
        }
        return transaction;
    }

    @Override
    public Transactions getTransactionById(int transactionId) {
        Transactions transaction = null;
        String sql = "SELECT * FROM transactions WHERE transactionId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                transaction = mapResultSetToTransaction(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving transaction by ID", e);
        }
        return transaction;
    }

    @Override
    public List<Transactions> getAllTransactions() {
        List<Transactions> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Transactions transaction = mapResultSetToTransaction(resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all transactions", e);
        }
        return transactions;
    }
    @Override
    public List<Transactions> getTransactionsByIds(List<Integer> transactionIds) {
        List<Transactions> transactions = new ArrayList<>();
        if (transactionIds.isEmpty()) {
            return transactions;
        }

        String sql = "SELECT * FROM transactions WHERE transactionId IN (";
        for (int i = 0; i < transactionIds.size(); i++) {
            sql += "?";
            if (i < transactionIds.size() - 1) {
                sql += ",";
            }
        }
        sql += ")";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < transactionIds.size(); i++) {
                preparedStatement.setInt(i + 1, transactionIds.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transactions transaction = mapResultSetToTransaction(resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


    @Override
    public Transactions updateTransaction(Transactions transaction) {
        String sql = "UPDATE transactions SET date = ?, type = ?, amount = ?, Currency_id = ?, source = ? WHERE transactionId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, transaction.getTransactionDate());
            preparedStatement.setString(2, transaction.getType());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getCurrencyId());
            preparedStatement.setString(5, transaction.getSource());
            preparedStatement.setInt(6, transaction.getTransactionId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating transaction", e);
        }
        return transaction;
    }

    @Override
    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transactionId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transactionId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting transaction", e);
        }
        return false;
    }

    private Transactions mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transactions transaction = new Transactions();
        transaction.setTransactionId(resultSet.getInt("transactionId"));
        transaction.setTransactionDate(resultSet.getDate("date"));
        transaction.setType(resultSet.getString("type"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setCurrencyId(resultSet.getString("Currency_id"));
        transaction.setSource(resultSet.getString("source"));
        return transaction;
    }
}
