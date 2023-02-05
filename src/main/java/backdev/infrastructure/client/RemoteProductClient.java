package backdev.infrastructure.client;

import backdev.domain.exception.EntityNotFoundException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public class RemoteProductClient {

    private final WebClient webClient;
    private final CircuitBreaker productBreaker;
    private final CircuitBreaker similarIdsBreaker;

    public RemoteProductClient(RemoteProductClientConfiguration configuration) {
        productBreaker = configuration.registry().circuitBreaker("product");
        similarIdsBreaker = configuration.registry().circuitBreaker("similarIds");

        webClient = WebClient.create(configuration.url());
    }

    public Mono<RemoteProduct> product(String productId) {
        return
            webClient
                .get()
                    .uri(builder -> builder.path("product/{product_id}").build(productId))
                .exchangeToMono(handleResponse(new ParameterizedTypeReference<RemoteProduct>() {}))
                .transformDeferred(CircuitBreakerOperator.of(productBreaker))
                .onErrorMap(WebClientResponseException.class, handleError());
    }

    public Flux<String> similarIds(String productId) {
        return
            webClient
                .get()
                    .uri(builder -> builder.path("product/{product_id}/similarids").build(productId))
                .exchangeToMono(handleResponse(new ParameterizedTypeReference<List<String>>() {}))
                .transformDeferred(CircuitBreakerOperator.of(similarIdsBreaker))
                .onErrorMap(WebClientResponseException.class, handleError())
                .flatMapMany(Flux::fromIterable);
    }

    private<T> Function<ClientResponse, Mono<T>> handleResponse(ParameterizedTypeReference<T> type) {
        return
            response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(type);
                }
                else {
                    return response.createError();
                }
            };
    }

    private Function<WebClientResponseException, Throwable> handleError() {
        return
            error -> {
                if (error.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return new EntityNotFoundException(error.getResponseBodyAsString());
                }
                else {
                    return error;
                }
            };
    }
}
