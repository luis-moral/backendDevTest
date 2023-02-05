package backdev.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SimilarProductsServiceShould {

    private ProductRepository productRepository;
    private SimilarProductsService similarProductsService;

    @BeforeEach
    public void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);

        similarProductsService = new SimilarProductsService(productRepository, 5);
    }

    @Test public void
    return_similar_products() {
        Product productTwo = new Product("2", "product2", 10, true);
        Product productThree = new Product("3", "product3", 12.5, false);

        Mockito
            .when(productRepository.findIdsSimilarTo("1"))
            .thenReturn(Flux.just("2", "3"));
        Mockito
            .when(productRepository.find("2"))
            .thenReturn(Mono.just(productTwo));
        Mockito
            .when(productRepository.find("3"))
            .thenReturn(Mono.just(productThree));

        StepVerifier
            .create(
                similarProductsService
                    .findProducts("1")
                    .collectList()
            )
            .assertNext(
                products ->
                    Assertions
                        .assertThat(products)
                        .containsExactlyInAnyOrder(productTwo, productThree)
            )
            .verifyComplete();

        Mockito
            .verify(productRepository, Mockito.times(1))
            .findIdsSimilarTo("1");
        Mockito
            .verify(productRepository, Mockito.times(1))
            .find("2");
        Mockito
            .verify(productRepository, Mockito.times(1))
            .find("3");
    }

    @Test public void
    return_similar_products_ignoring_failing_to_retrieve() {
        Product productTwo = new Product("2", "product2", 10, true);
        Product productFour = new Product("4", "product4", 15.25, true);

        Mockito
            .when(productRepository.findIdsSimilarTo("1"))
            .thenReturn(Flux.just("2", "3", "4"));
        Mockito
            .when(productRepository.find("2"))
            .thenReturn(Mono.just(productTwo));
        Mockito
            .when(productRepository.find("3"))
            .thenThrow(new RuntimeException("Some Error"));
        Mockito
            .when(productRepository.find("4"))
            .thenReturn(Mono.just(productFour));

        StepVerifier
            .create(
                similarProductsService
                    .findProducts("1")
                    .collectList()
            )
            .assertNext(
                products ->
                    Assertions
                        .assertThat(products)
                        .containsExactlyInAnyOrder(productTwo, productFour)
            )
            .verifyComplete();

        Mockito
            .verify(productRepository, Mockito.times(1))
            .findIdsSimilarTo("1");
        Mockito
            .verify(productRepository, Mockito.times(1))
            .find("2");
        Mockito
            .verify(productRepository, Mockito.times(1))
            .find("3");
        Mockito
            .verify(productRepository, Mockito.times(1))
            .find("4");
    }
}