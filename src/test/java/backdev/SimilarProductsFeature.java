package backdev;

import backdev.infrastructure.handler.product.similar.GetSimilarProductsItemResponse;
import backdev.test.RemoteProductServiceMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SimilarProductsFeature {

    private static RemoteProductServiceMock remoteProductService;

    @Value("${endpoint.product.path.similar}")
    private String similarProductsEndpoint;

    @Autowired
    private WebTestClient webClient;

    @BeforeAll
    public static void beforeAll() {
        remoteProductService = new RemoteProductServiceMock(43001);
        remoteProductService.start();
    }

    @AfterAll
    public static void afterAll() {
        remoteProductService.stop();
    }

    @Test public void
    return_similar_products() {
        webClient
            .get()
                .uri(builder -> builder.path(similarProductsEndpoint).build("1"))
            .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(new ParameterizedTypeReference<List<GetSimilarProductsItemResponse>>() {})
                    .consumeWith(response -> {
                            List<GetSimilarProductsItemResponse> items = response.getResponseBody();

                            Assertions
                                .assertThat(items)
                                .containsExactlyInAnyOrder(
                                    new GetSimilarProductsItemResponse("2", "product2", 10.5, true),
                                    new GetSimilarProductsItemResponse("3", "product3", 12, false),
                                    new GetSimilarProductsItemResponse("4", "product4", 15.25, true)
                                );
                        }
                    );
    }

    @Test public void
    return_bad_request_if_product_does_not_exists() {
        webClient
            .get()
                .uri(builder -> builder.path(similarProductsEndpoint).build("2500"))
            .exchange()
                .expectStatus()
                    .isBadRequest();
    }

    @Test public void
    caches_calls_to_remote_product_service() {
        IntStream
            .range(0, 5)
            .forEach(
                value ->
                    webClient
                        .get()
                            .uri(builder -> builder.path(similarProductsEndpoint).build("100"))
                        .exchange()
                            .expectStatus()
                                .isOk()
            );

        remoteProductService.verifySimilarIds(1, "100");
        remoteProductService.verifyProductDetails(1, "5");
    }
}
