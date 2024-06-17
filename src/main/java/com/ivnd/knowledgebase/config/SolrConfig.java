package com.ivnd.knowledgebase.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

@Configuration
public class SolrConfig {
    @Value("${solr.host}")
    private String url;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder(url).build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
