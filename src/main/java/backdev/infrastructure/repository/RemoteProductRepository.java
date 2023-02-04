package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.domain.product.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RemoteProductRepository implements ProductRepository {

    @Override
    public Mono<Product> find(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Flux<String> findIdsSimilarTo(String productId) {
        throw new UnsupportedOperationException();
    }
}
