package backdev.infrastructure.configuration;

import backdev.infrastructure.client.RemoteProductClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Value("${client.product.url}")
    private String clientProductUrl;

    @Bean
    public RemoteProductClient remoteProductClient() {
        return new RemoteProductClient(clientProductUrl);
    }
}
