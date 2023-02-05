package backdev.domain.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.logging.Level;

public class SimilarProductsService {

    private final ProductRepository productRepository;
    private final int parallelism;

    public SimilarProductsService(
        ProductRepository productRepository,
        int parallelism
    ) {
        this.productRepository = productRepository;
        this.parallelism = parallelism;
    }

    public Flux<Product> findProducts(String productId) {
        return
            productRepository
                .findIdsSimilarTo(productId)
                .parallel(parallelism)
                .runOn(Schedulers.boundedElastic())
                .flatMap(similarId ->
                    Mono
                        .defer(() -> productRepository.find(similarId))
                        .onErrorResume(error -> Mono.empty())
                )
                .sequential()
                .log(getClass().getName(), Level.FINE);
    }
}
