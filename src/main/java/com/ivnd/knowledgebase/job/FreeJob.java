package com.ivnd.knowledgebase.job;

import com.ivnd.knowledgebase.common.CronJob;
import com.ivnd.knowledgebase.config.job.JobProperties;
import com.ivnd.knowledgebase.service.TaskScheduleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 17/05/2024
 */
@Component
@Log4j2
public class FreeJob extends CronJob {
    private final JobProperties jobProperties;

    public FreeJob(TaskScheduleService taskScheduleService,
                   JobProperties jobProperties) {
        super(UUID.randomUUID().toString(), taskScheduleService);
        this.jobProperties = jobProperties;
    }

    @Override
    protected String getExpression() {
        return jobProperties.getCronCreate();
    }

    @Override
    protected void doWork() {
        log.info("dhaskdhkashdkas");
    }
}
