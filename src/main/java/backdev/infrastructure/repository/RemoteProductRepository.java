package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.domain.product.ProductRepository;
import backdev.infrastructure.client.RemoteProductClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RemoteProductRepository implements ProductRepository {

    private final RemoteProductClient client;
    private final RemoteProductRepositoryMapper mapper;

    public RemoteProductRepository(RemoteProductClient client, RemoteProductRepositoryMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> find(String id) {
        return
            client
                .product(id)
                .map(mapper::toProduct);
    }

    @Override
    public Flux<String> findIdsSimilarTo(String productId) {
        return client.similarIds(productId);
    }
}
