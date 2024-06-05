package atlantafx.sampler.services.serviceImpl;


import atlantafx.sampler.DBManager;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.services.EventService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    private Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getLong("id"));
        event.setName(resultSet.getString("name"));
        return event;
    }
}
