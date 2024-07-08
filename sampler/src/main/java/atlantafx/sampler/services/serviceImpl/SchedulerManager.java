package atlantafx.sampler.services.serviceImpl;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerManager {

    public static void startScheduler() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(EventReminderJob.class)
                .withIdentity("eventReminderJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(22, 9)) // Run daily at 9 AM
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

}
