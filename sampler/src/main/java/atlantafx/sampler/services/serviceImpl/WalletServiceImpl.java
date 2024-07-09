package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Wallet;
import atlantafx.sampler.services.WalletService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletServiceImpl implements WalletService {

    private static final Logger logger = Logger.getLogger(WalletService.class.getName());

    public Wallet createWallet(Wallet wallet) {
        String sql = "INSERT INTO wallet (userId, balence, currency) VALUES (?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, wallet.getUserId());
            preparedStatement.setDouble(2, wallet.getBalance());
            preparedStatement.setString(3, wallet.getCurrency());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Wallet created successfully");
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        wallet.setWalletId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating wallet", e);
        }
        return wallet;
    }

    public Wallet getWalletById(int walletId) {
        Wallet wallet = null;
        String sql = "SELECT * FROM wallet WHERE walletId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, walletId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                wallet = mapResultSetToWallet(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving wallet by ID", e);
        }
        return wallet;
    }

    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM wallet";

        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Wallet wallet = mapResultSetToWallet(resultSet);
                wallets.add(wallet);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all wallets", e);
        }
        return wallets;
    }

    public Wallet updateWallet(Wallet wallet) {
        String sql = "UPDATE wallet SET userId = ?, balence = ?, currency = ? WHERE walletId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, wallet.getUserId());
            preparedStatement.setDouble(2, wallet.getBalance());
            preparedStatement.setString(3, wallet.getCurrency());
            preparedStatement.setInt(4, wallet.getWalletId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating wallet", e);
        }
        return wallet;
    }

    public boolean deleteWallet(int walletId) {
        String sql = "DELETE FROM wallet WHERE walletId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, walletId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting wallet", e);
        }
        return false;
    }

    private Wallet mapResultSetToWallet(ResultSet resultSet) throws SQLException {
        Wallet wallet = new Wallet();
        wallet.setWalletId(resultSet.getInt("walletId"));
        wallet.setUserId(resultSet.getInt("userId"));
        wallet.setBalance(resultSet.getDouble("balence"));
        wallet.setCurrency(resultSet.getString("currency"));
        return wallet;
    }
}
