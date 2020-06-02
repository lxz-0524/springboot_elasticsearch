package com.usian.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig extends ElasticsearchProperties {

    @Bean
    public RestHighLevelClient getRestHighLevelClient(){
        String[] hosts = getClusterNodes().split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String host = hosts[i];
            httpHosts[i] = new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]));
        }
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }
}
