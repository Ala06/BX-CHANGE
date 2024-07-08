package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Discussions;
import atlantafx.sampler.services.DiscussionService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscussionServiceImpl implements DiscussionService {
    @Override
    public List<Discussions> getAllDiscussions() {
        return getDiscussionsByQuery("SELECT * FROM discussions");
    }

    @Override
    public List<Discussions> getDiscussionsByUserId(int userId) {
        String sql = "SELECT * FROM discussions WHERE user1 = ? OR user2 = ?";
        return getDiscussionsByQuery(sql, userId, userId);
    }

    @Override
    public Discussions getDiscussionById(int discussionId) {
        String sql = "SELECT * FROM discussions WHERE discussionId = ?";
        List<Discussions> discussions = getDiscussionsByQuery(sql, discussionId);
        return discussions.isEmpty() ? null : discussions.get(0);
    }

    @Override
    public Discussions createDiscussion(int user1Id, int user2Id) {
        String sql = "INSERT INTO discussions (user1, user2) VALUES (?, ?)";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, user1Id);
            preparedStatement.setInt(2, user2Id);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Discussions discussion = new Discussions();
                    discussion.setDiscussionId(generatedKeys.getInt(1));
                    discussion.setUser1Id(user1Id);
                    discussion.setUser2Id(user2Id);
                    return discussion;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Discussions findOrCreateDiscussion(int user1Id, int user2Id) {
        String sql = "SELECT * FROM discussions WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)";
        List<Discussions> discussions = getDiscussionsByQuery(sql, user1Id, user2Id, user2Id, user1Id);
        if (!discussions.isEmpty()) {
            return discussions.get(0);
        }
        return createDiscussion(user1Id, user2Id);
    }

    private List<Discussions> getDiscussionsByQuery(String sql, Object... params) {
        List<Discussions> discussions = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Discussions discussion = new Discussions();
                discussion.setDiscussionId(resultSet.getInt("discussionId"));
                discussion.setUser1Id(resultSet.getInt("user1"));
                discussion.setUser2Id(resultSet.getInt("user2"));
                discussions.add(discussion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }
}