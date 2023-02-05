package backdev.infrastructure.client;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public record RemoteProductClientConfiguration(
    String url,
    CircuitBreakerRegistry registry
) {}
