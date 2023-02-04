package backdev;

import backdev.infrastructure.handler.product.similar.GetSimilarProductsItemResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class SimilarProductsFeature {

    @Value("${endpoint.product.path.similar}")
    private String similarProductsEndpoint;

    @Autowired
    private WebTestClient webClient;

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
                                    new GetSimilarProductsItemResponse("2", "product2", 10.0, true),
                                    new GetSimilarProductsItemResponse("3", "product3", 15.0, false),
                                    new GetSimilarProductsItemResponse("4", "product4", 17.50, true)
                                );
                        }
                    );
    }
}
