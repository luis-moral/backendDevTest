package backdev.infrastructure.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .exchangeToMono(
                    clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(RemoteProduct.class);
                        }
                        else {
                            return clientResponse.createError();
                        }
                    }
                );
    }

    @CircuitBreaker(name = "similarIds")
    public Flux<String> similarIds(String productId) {
        return
            webClient
                .get()
                .uri(builder -> builder.path("product/{product_id}/similarids").build(productId))
                .exchangeToMono(
                    clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(new ParameterizedTypeReference<List<String>>() {});
                        }
                        else {
                            return clientResponse.createError();
                        }
                    }
                )
                .flatMapMany(Flux::fromIterable);
    }
}
