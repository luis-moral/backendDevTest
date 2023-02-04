package backdev.configuration;

import backdev.handler.status.get.GetStatusHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public GetStatusHandler getStatusHandler() {
        return new GetStatusHandler();
    }
}
