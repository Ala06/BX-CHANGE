package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Messages;
import atlantafx.sampler.services.MessageService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageServiceImpl implements MessageService {
    @Override
    public List<Messages> getAllMessages() {
        return getMessagesByQuery("SELECT * FROM messages");
    }

    @Override
    public List<Messages> searchMessagesByText(String text) {
        String sql = "SELECT * FROM messages WHERE content LIKE ?";
        return getMessagesByQuery(sql, "%" + text + "%");
    }

    private List<Messages> getMessagesByQuery(String sql, Object... params) {
        List<Messages> messages = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Messages message = mapResultSetToMessages(resultSet);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Messages getMessageById(int messageId) {
        String sql = "SELECT * FROM messages WHERE messageId = ?";
        List<Messages> messages = getMessagesByQuery(sql, messageId);
        return messages.isEmpty() ? null : messages.get(0);
    }


    @Override
    public void addMessage(Messages message) {
        String sql = "INSERT INTO messages (date, content) VALUES (?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, new Date(message.getDate().getTime()));
            preparedStatement.setString(2, message.getContent());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMessage(Messages message) {
        String sql = "UPDATE messages SET date = ?, content = ? WHERE messageId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, new Date(message.getDate().getTime()));
            preparedStatement.setString(2, message.getContent());
            preparedStatement.setInt(3, message.getMessageId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(int messageId) {
        String sql = "DELETE FROM messages WHERE messageId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Messages mapResultSetToMessages(ResultSet resultSet) throws SQLException {
        Messages message = new Messages();
        message.setMessageId(resultSet.getInt("messageId"));
        message.setDate(resultSet.getDate("date"));
        message.setContent(resultSet.getString("content"));
        return message;
    }
}
