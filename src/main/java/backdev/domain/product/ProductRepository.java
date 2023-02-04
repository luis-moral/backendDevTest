package backdev.domain.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> find(String id);

    Flux<String> findIdsSimilarTo(String productId);
}
