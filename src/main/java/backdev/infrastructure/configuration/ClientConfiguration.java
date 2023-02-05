package backdev.infrastructure.configuration;

import backdev.infrastructure.client.RemoteProductClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public RemoteProductClient remoteProductClient() {
        return new RemoteProductClient();
    }
}
