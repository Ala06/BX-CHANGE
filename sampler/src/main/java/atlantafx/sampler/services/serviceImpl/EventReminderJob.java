package atlantafx.sampler.services.serviceImpl;

import atlantafx.sampler.entities.Event;
import atlantafx.sampler.entities.EventFollower;
import atlantafx.sampler.services.EventFollowerService;
import atlantafx.sampler.services.EventService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EventReminderJob implements Job {

    private final EventService eventService = new EventServiceImpl();
    private final EventFollowerService eventFollowerService = new EventFollowerServiceImpl();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Executing event reminder job...");

        // Get tomorrow's date
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Date tomorrowDate = Date.valueOf(tomorrow);

        // Fetch events happening tomorrow
        List<Event> events = eventService.getEventsByDate(tomorrowDate);

        // Log the number of events fetched
        System.out.println("Number of events happening tomorrow: " + events.size());

        // Map to hold the events by followers
        Map<Integer, List<Event>> followerEventsMap = new HashMap<>();

        // For each event, fetch subscribed users and group events by followers
        for (Event event : events) {
            System.out.println("Processing event: " + event.getTitle());

            List<EventFollower> followers = eventFollowerService.getEventFollowersByEventId(event.getEventId());

            // Log the number of followers for the event
            System.out.println("Number of followers for event " + event.getTitle() + ": " + followers.size());

            for (EventFollower follower : followers) {
                followerEventsMap
                        .computeIfAbsent(follower.getUserId(), k -> new ArrayList<>())
                        .add(event);
            }
        }

        // Send an email to each follower with their events
        for (Map.Entry<Integer, List<Event>> entry : followerEventsMap.entrySet()) {
            Integer followerId = entry.getKey();
            List<Event> followerEvents = entry.getValue();

            // Assuming EventFollower has an email field
            String toAddress = getFollowerEmailById(followerId);
            if (toAddress == null) {
                continue; // Skip if no email found for follower
            }

            String subject = "Event Reminder: Events Happening Tomorrow";
            StringBuilder message = new StringBuilder("Dear User,\n\nThis is a reminder that the following events are happening tomorrow:\n");

            for (Event event : followerEvents) {
                message.append("- ").append(event.getTitle()).append("\n");
            }

            message.append("\nBest regards,\nBX-CHANGE Team");

            try {
                EmailUtil.sendEmail(toAddress, subject, message.toString());
                System.out.println("Email sent to: " + toAddress);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to send email to: " + toAddress + " Error: " + e.getMessage());
            }
        }
    }

    // Helper method to get follower email by userId
    private String getFollowerEmailById(int userId) {

        return "alaabada11@gmail.com";
    }
}
