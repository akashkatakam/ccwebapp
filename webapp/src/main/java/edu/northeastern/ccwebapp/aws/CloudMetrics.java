package edu.northeastern.ccwebapp.aws;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class CloudMetrics {
    @Value("${publish.metrics}")
    private boolean publishMetrics;

//    @Value("${metrics.server.hostname}")
//    private String metricsServerHost;
//
//    @Value("${metrics.server.port}")
//    private int metricsServerPort;

    @Bean
    public StatsDClient metricsClient() {

        if (publishMetrics) {
            return new NonBlockingStatsDClient("csye6225", "localhost", 8125);
        }

        return new NoOpStatsDClient();
    }
}
