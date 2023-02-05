package backdev.infrastructure.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RemoteProductClient {

    public Mono<RemoteProduct> product(String productId) {
        throw new UnsupportedOperationException();
    }

    public Flux<String> similarIds(String productId) {
        throw new UnsupportedOperationException();
    }
}
