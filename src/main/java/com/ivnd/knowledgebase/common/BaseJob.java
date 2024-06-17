package com.ivnd.knowledgebase.common;

import com.ivnd.knowledgebase.service.TaskScheduleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.Trigger;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 17/05/2024
 */
public abstract class BaseJob implements InitializingBean {
    private final String jobId;
    private final TaskScheduleService taskScheduleService;

    protected BaseJob(String jobId, TaskScheduleService taskScheduleService) {
        this.jobId = jobId;
        this.taskScheduleService = taskScheduleService;
    }

    protected abstract void doWork();

    protected abstract Trigger getTrigger();

    protected void addJob() {
        taskScheduleService.scheduleTask(jobId, this::doWork, this.getTrigger());
    }

    protected void removeJob() {
        taskScheduleService.removeScheduleTask(jobId);
    }

    protected void reloadJob() {
        this.removeJob();
        this.addJob();
    }

    @Override
    public void afterPropertiesSet() {
        this.addJob();
    }
}
