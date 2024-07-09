package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.entities.TransactionList;
import atlantafx.sampler.services.TransactionListService;
import atlantafx.sampler.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionListServiceImpl implements TransactionListService {

    @Override
    public void addTransactionToWallet(int transactionId, int walletId) {
        String sql = "INSERT INTO transaction_list (transactionId, walletId) VALUES (?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transactionId);
            preparedStatement.setInt(2, walletId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getTransactionsByWalletId(int walletId) {
        List<Integer> transactionIds = new ArrayList<>();
        String sql = "SELECT transactionId FROM transaction_list WHERE walletId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, walletId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                transactionIds.add(resultSet.getInt("transactionId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionIds;
    }

    @Override
    public boolean removeTransactionFromWallet(int transactionId, int walletId) {
        String sql = "DELETE FROM transaction_list WHERE transactionId = ? AND walletId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transactionId);
            preparedStatement.setInt(2, walletId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
