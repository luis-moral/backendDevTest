package backdev;

import backdev.infrastructure.handler.product.similar.GetSimilarProductsItemResponse;
import backdev.test.RemoteProductServerMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private RemoteProductServerMock remoteProductServer;

    @Value("${endpoint.product.path.similar}")
    private String similarProductsEndpoint;

    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    public void setUp() {
        remoteProductServer = new RemoteProductServerMock(43001);
        remoteProductServer.start();
    }

    @AfterEach
    public void tearDown() {
        remoteProductServer.stop();
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
}
