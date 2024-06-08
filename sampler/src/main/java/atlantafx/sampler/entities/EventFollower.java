package atlantafx.sampler.entities;

public class EventFollower {

    private int eventId;
    private int userId;


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public EventFollower(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public EventFollower() {
    }
}
