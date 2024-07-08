package atlantafx.sampler.services;

import atlantafx.sampler.entities.EventFollower;

import java.util.List;

public interface EventFollowerService {
    List<EventFollower> getAllEventFollowers();
    EventFollower getEventFollowerById(int eventId, int userId);
    void addEventFollower(EventFollower eventFollower);
    void deleteEventFollower(int eventId, int userId);
    List<EventFollower> getEventFollowersByEventId(int eventId);
}
