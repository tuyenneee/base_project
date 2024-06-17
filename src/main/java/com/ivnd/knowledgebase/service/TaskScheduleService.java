package com.ivnd.knowledgebase.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 17/05/2024
 */
@Service
@Log4j2
public class TaskScheduleService {
    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture<?>> jobsMap;

    public TaskScheduleService(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
        this.jobsMap = new HashMap<>();
    }

    public void scheduleTask(String jobId, Runnable task, Trigger trigger) {
        log.debug("Scheduling task with job id = {}", jobId);
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task, trigger);
        jobsMap.put(jobId, scheduledTask);
    }

    public void removeScheduleTask(String jobId) {
        log.debug("Remove schedule task with job id = {}", jobId);
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);
        }
    }
}
