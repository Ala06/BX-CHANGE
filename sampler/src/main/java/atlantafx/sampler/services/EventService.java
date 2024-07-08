package atlantafx.sampler.services;

import atlantafx.sampler.entities.Event;

import java.util.List;

public interface EventService {

    List<Event> getAllEvents();
    Event getEventById(int eventId);
    void addEvent(Event event);
    void updateEvent(Event event);
    void deleteEvent(int eventId);
}
