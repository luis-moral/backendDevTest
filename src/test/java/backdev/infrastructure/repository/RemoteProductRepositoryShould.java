package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.infrastructure.client.RemoteProduct;
import backdev.infrastructure.client.RemoteProductClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class RemoteProductRepositoryShould {

    private RemoteProductClient client;
    private RemoteProductRepositoryMapper mapper;
    private RemoteProductRepository repository;

    @BeforeEach
    public void setUp() {
        client = Mockito.mock(RemoteProductClient.class);
        mapper = Mockito.mock(RemoteProductRepositoryMapper.class);

        repository = new RemoteProductRepository(client, mapper);
    }

    @Test public void
    find_product_details() {
        RemoteProduct remoteProductOne = new RemoteProduct("1", "product1", 10.0, true);
        Product productOne = new Product("1", "product1", 10.0, true);

        Mockito
            .when(client.product("1"))
            .thenReturn(Mono.just(remoteProductOne));

        Mockito
            .when(mapper.toProduct(remoteProductOne))
            .thenReturn(productOne);

        StepVerifier
            .create(repository.find("1"))
            .expectNext(productOne)
            .verifyComplete();

        Mockito
            .verify(client, Mockito.times(1))
            .product(Mockito.any());

        Mockito
            .verify(mapper, Mockito.times(1))
            .toProduct(Mockito.any());
    }

    @Test public void
    find_similar_product_ids() {
        List<String> similarIds = List.of("2", "3", "4");

        Mockito
            .when(client.similarIds("1"))
            .thenReturn(Flux.fromIterable(similarIds));

        StepVerifier
            .create(repository.findIdsSimilarTo("1").collectList())
            .assertNext(ids ->
                Assertions
                    .assertThat(ids)
                    .containsExactlyInAnyOrderElementsOf(similarIds)
            )
            .verifyComplete();

        Mockito
            .verify(client, Mockito.times(1))
            .similarIds(Mockito.any());
    }
}