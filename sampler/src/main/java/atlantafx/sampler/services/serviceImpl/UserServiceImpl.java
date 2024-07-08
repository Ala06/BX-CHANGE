package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.entities.Role;
import atlantafx.sampler.entities.User;
import atlantafx.sampler.services.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final List<User> users = new ArrayList<>();


    @Override
    public void createUser(User user) throws SQLException{
        String sql = "INSERT INTO user (nom, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE userid = ?")) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM user")) {

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE user SET nom = ?, email = ?, password = ? WHERE userid = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE userid = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User u = new User();
        u.setUserID(resultSet.getInt("userId"));
        u.setRole(Role.valueOf(resultSet.getString("role")));
        u.setEmail(resultSet.getString("email"));
        u.setName(resultSet.getString("nom"));

        return u;
    }

}
