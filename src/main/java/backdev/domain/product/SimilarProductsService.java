package backdev.domain.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.logging.Level;

public class SimilarProductsService {

    private final ProductRepository productRepository;
    private final int concurrency;

    public SimilarProductsService(
        ProductRepository productRepository,
        int concurrency
    ) {
        this.productRepository = productRepository;
        this.concurrency = concurrency;
    }

    public Flux<Product> findProducts(String productId) {
        return
            productRepository
                .findIdsSimilarTo(productId)
                .flatMapSequential(similarId ->
                    Mono
                        .defer(() -> productRepository.find(similarId))
                        .publishOn(Schedulers.boundedElastic())
                        .onErrorResume(error -> Mono.empty())
                        .log(getClass().getName() + "_" + similarId, Level.FINE),
                    concurrency
                )
                .log(getClass().getName(), Level.FINE);
    }
}
