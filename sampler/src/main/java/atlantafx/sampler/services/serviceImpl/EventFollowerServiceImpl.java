package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.EventFollower;
import atlantafx.sampler.services.EventFollowerService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventFollowerServiceImpl implements EventFollowerService {

    @Override
    public List<EventFollower> getAllEventFollowers() {
        List<EventFollower> eventFollowers = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM event_followers")) {

            while (resultSet.next()) {
                EventFollower eventFollower = mapResultSetToEventFollower(resultSet);
                eventFollowers.add(eventFollower);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventFollowers;
    }

    @Override
    public EventFollower getEventFollowerById(int eventId, int userId) {
        EventFollower eventFollower = null;
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM event_followers WHERE eventId = ? AND userId = ?")) {

            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                eventFollower = mapResultSetToEventFollower(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventFollower;
    }

    @Override
    public void addEventFollower(EventFollower eventFollower) {
        String sql = "INSERT INTO event_followers (eventId, userId) VALUES (?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, eventFollower.getEventId());
            preparedStatement.setInt(2, eventFollower.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEventFollower(int eventId, int userId) {
        String sql = "DELETE FROM event_followers WHERE eventId = ? AND userId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private EventFollower mapResultSetToEventFollower(ResultSet resultSet) throws SQLException {
        int eventId = resultSet.getInt("eventId");
        int userId = resultSet.getInt("userId");
        return new EventFollower(eventId, userId);
    }
}
