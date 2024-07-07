package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.services.EventService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM events")) {

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Event getEventById(int eventId) {
        Event event = null;
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events WHERE eventId = ?")) {

            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                event = mapResultSetToEvent(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public void addEvent(Event event) {
        String sql = "INSERT INTO events (date, title, description, duration, image) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, new java.sql.Date(event.getDate().getTime()));
            preparedStatement.setString(2, event.getTitle());
            preparedStatement.setString(3, event.getDescription());
            preparedStatement.setInt(4, event.getDuration());
            preparedStatement.setString(5, event.getImage());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEvent(Event event) {
        String sql = "UPDATE events SET date = ?, title = ?, description = ?, duration = ?, image = ? WHERE eventId = ?";

        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, new java.sql.Date(event.getDate().getTime()));
            preparedStatement.setString(2, event.getTitle());
            preparedStatement.setString(3, event.getDescription());
            preparedStatement.setInt(4, event.getDuration());
            preparedStatement.setString(5, event.getImage());
            preparedStatement.setInt(6, event.getEventId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        try (Connection connection = DBManager.getConnection()) {
            // Step 1: Delete event followers
            String deleteFollowersSql = "DELETE FROM event_followers WHERE eventId = ?";
            try (PreparedStatement deleteFollowersStmt = connection.prepareStatement(deleteFollowersSql)) {
                deleteFollowersStmt.setInt(1, eventId);
                deleteFollowersStmt.executeUpdate();
            }

            // Step 2: Delete event
            String deleteEventSql = "DELETE FROM events WHERE eventId = ?";
            try (PreparedStatement deleteEventStmt = connection.prepareStatement(deleteEventSql)) {
                deleteEventStmt.setInt(1, eventId);
                deleteEventStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setEventId(resultSet.getInt("eventId"));
        event.setDate(resultSet.getDate("date"));
        event.setTitle(resultSet.getString("title"));
        event.setDescription(resultSet.getString("description"));
        event.setDuration(resultSet.getInt("duration"));
        event.setImage(resultSet.getString("image"));
        return event;
    }

    @Override
    public List<Event> getEventsByDate(Date date) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE date = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("eventId"),
                        resultSet.getDate("date"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("duration"),
                        resultSet.getString("image")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
