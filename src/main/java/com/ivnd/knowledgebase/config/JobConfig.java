package com.ivnd.knowledgebase.config;

import com.ivnd.knowledgebase.config.job.JobProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 17/05/2024
 */
@Configuration
public class JobConfig {

    @Bean
    @ConfigurationProperties("job")
    public JobProperties jobProperties() {
        return new JobProperties();
    }
}
