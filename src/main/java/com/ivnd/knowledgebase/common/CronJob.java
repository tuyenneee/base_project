package com.ivnd.knowledgebase.common;

import com.ivnd.knowledgebase.service.TaskScheduleService;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 17/05/2024
 */
public abstract class CronJob extends BaseJob {
    protected CronJob(String jobId, TaskScheduleService taskScheduleService) {
        super(jobId, taskScheduleService);
    }

    protected abstract String getExpression();

    @Override
    protected Trigger getTrigger() {
        return new CronTrigger(this.getExpression());
    }
}
