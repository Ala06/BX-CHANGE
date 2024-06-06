package atlantafx.sampler.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Event {
    private int eventId;
    private Date date;
    private String title;
    private String description;
    private int duration;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Event(Date date, String title, String description, int duration) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public Event() {
    }
}
