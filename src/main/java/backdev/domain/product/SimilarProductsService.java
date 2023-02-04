package backdev.domain.product;

import reactor.core.publisher.Flux;

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
                .flatMap(productRepository::find, concurrency)
                .log(getClass().getName(), Level.FINE);
    }
}
