package backdev.infrastructure.client;

import backdev.domain.exception.EntityNotFoundException;
import backdev.test.RemoteProductServiceMock;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class RemoteProductClientShould {

    private final static AtomicInteger PORT = new AtomicInteger(50_000);

    private CircuitBreaker productBreaker;
    private CircuitBreaker similarIdsBreaker;
    private RemoteProductServiceMock server;
    private RemoteProductClient client;
    
    @BeforeEach
    public void setUp() {
        CircuitBreakerRegistry registry = createRegistry();
        productBreaker = registry.circuitBreaker("product");
        similarIdsBreaker = registry.circuitBreaker("similarIds");

        server = new RemoteProductServiceMock(PORT.getAndIncrement());
        server.start();

        client = new RemoteProductClient(server.url(), productBreaker, similarIdsBreaker);
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test public void
    get_product_details() {
        RemoteProduct productTwo = new RemoteProduct("2", "product2", 10.5, true);

        StepVerifier
            .create(client.product("2"))
            .expectNext(productTwo)
            .verifyComplete();

        server.verifyProductDetails(1, "2");
    }

    @Test public void
    return_domain_exception_if_product_details_not_found() {
        StepVerifier
            .create(client.product("25"))
            .consumeErrorWith(
                error ->
                    Assertions
                        .assertThat(error)
                        .isInstanceOf(EntityNotFoundException.class)
            )
            .verify();
    }

    @Test public void
    open_product_circuit_if_too_many_errors() {
        checkCircuitBreaker(productBreaker, client.product("25"));
    }

    @Test public void
    get_similar_product_ids() {
        List<String> similarIds = List.of("2", "3", "4");

        StepVerifier
            .create(client.similarIds("1").collectList())
            .assertNext(
                ids ->
                    Assertions
                        .assertThat(ids)
                        .containsExactlyInAnyOrderElementsOf(similarIds)
            )
            .verifyComplete();

        server.verifySimilarIds(1, "1");
    }

    @Test public void
    return_domain_exception_if_similar_product_ids_not_found() {
        StepVerifier
            .create(client.similarIds("25"))
            .consumeErrorWith(
                error ->
                    Assertions
                        .assertThat(error)
                        .isInstanceOf(EntityNotFoundException.class)
            )
            .verify();
    }

    @Test public void
    open_similar_ids_circuit_if_too_many_errors() {
        checkCircuitBreaker(similarIdsBreaker, client.similarIds("25"));
    }

    private void checkCircuitBreaker(CircuitBreaker circuitBreaker, Publisher<?> publisher) {
        server.stop();

        Assertions
            .assertThat(circuitBreaker.getState())
            .isEqualTo(CircuitBreaker.State.CLOSED);

        IntStream
            .range(0, 5)
            .forEach(value ->
                StepVerifier
                    .create(publisher)
                    .expectError()
                    .verify()
            );

        Assertions
            .assertThat(circuitBreaker.getState())
            .isEqualTo(CircuitBreaker.State.OPEN);
    }

    private CircuitBreakerRegistry createRegistry() {
        CircuitBreakerConfig configuration =
            CircuitBreakerConfig
                .custom()
                .slidingWindowSize(5)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .minimumNumberOfCalls(1)
                .failureRateThreshold(50.0f)
                .build();

        return CircuitBreakerRegistry.of(configuration);
    }
}