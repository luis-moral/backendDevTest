package backdev.infrastructure.configuration;

import backdev.infrastructure.client.RemoteProductClient;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Value("${client.product.url}")
    private String clientProductUrl;

    @Bean
    public RemoteProductClient remoteProductClient(CircuitBreakerRegistry registry) {
        return
            new RemoteProductClient(
                clientProductUrl,
                registry.circuitBreaker("product"),
                registry.circuitBreaker("similarIds")
            );
    }
}
