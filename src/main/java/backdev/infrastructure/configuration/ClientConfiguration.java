package backdev.infrastructure.configuration;

import backdev.infrastructure.client.RemoteProductClient;
import backdev.infrastructure.client.RemoteProductClientConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ClientConfiguration {

    @Value("${client.product.url}")
    private String clientProductUrl;

    @Bean
    public RemoteProductClient remoteProductClient(CircuitBreakerRegistry circuitBreakerRegistry) {
        return
            new RemoteProductClient(
                new RemoteProductClientConfiguration(
                    clientProductUrl,
                    circuitBreakerRegistry
                )
            );
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfiguration =
            CircuitBreakerConfig
                .custom()
                .slidingWindowSize(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50.0f)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfiguration);
    }
}
