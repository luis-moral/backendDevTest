package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.domain.product.ProductRepository;
import backdev.infrastructure.client.RemoteProductClient;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class RemoteProductRepository implements ProductRepository {

    private final AsyncLoadingCache<String, Product> productCache;
    private final AsyncLoadingCache<String, List<String>> similarIdsCache;

    public RemoteProductRepository(
        RemoteProductClient client,
        long cacheDuration,
        RemoteProductRepositoryMapper mapper
    ) {
        productCache =
            Caffeine
                .newBuilder()
                .expireAfterWrite(Duration.ofMillis(cacheDuration))
                .buildAsync(
                    (productId, executor) ->
                        client
                            .product(productId)
                            .map(mapper::toProduct)
                            .toFuture()
                );

        similarIdsCache =
            Caffeine
                .newBuilder()
                .expireAfterWrite(Duration.ofMillis(cacheDuration))
                .buildAsync(
                    (productId, executor) ->
                        client
                            .similarIds(productId)
                            .collectList()
                            .toFuture()
                );
    }

    @Override
    public Mono<Product> find(String id) {
        return Mono.fromFuture(productCache.get(id));
    }

    @Override
    public Flux<String> findIdsSimilarTo(String productId) {
        return
            Mono
                .fromFuture(similarIdsCache.get(productId))
                .flatMapMany(Flux::fromIterable);
    }
}
