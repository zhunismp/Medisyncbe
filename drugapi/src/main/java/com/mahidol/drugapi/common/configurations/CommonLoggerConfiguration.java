package com.mahidol.drugapi.common.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class CommonLoggerConfiguration {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(20000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);

        filter.setBeforeMessagePrefix("Incoming Request: [");
        filter.setBeforeMessageSuffix("]");
        filter.setAfterMessagePrefix("Completed Request: [");
        filter.setAfterMessageSuffix("]");

        return filter;
    }
}
