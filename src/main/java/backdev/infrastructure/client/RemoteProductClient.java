package backdev.infrastructure.client;

import backdev.domain.exception.EntityNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    public RemoteProductClient(String url) {
        webClient = WebClient.create(url);
    }

    @CircuitBreaker(name = "product")
    public Mono<RemoteProduct> product(String productId) {
        return
            webClient
                .get()
                    .uri(builder -> builder.path("product/{product_id}").build(productId))
                .exchangeToMono(handleResponse(new ParameterizedTypeReference<RemoteProduct>() {}))
                .onErrorMap(WebClientResponseException.class, handleError());
    }

    @CircuitBreaker(name = "similarIds")
    public Flux<String> similarIds(String productId) {
        return
            webClient
                .get()
                    .uri(builder -> builder.path("product/{product_id}/similarids").build(productId))
                .exchangeToMono(handleResponse(new ParameterizedTypeReference<List<String>>() {}))
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
