package backdev.infrastructure.client;

import backdev.test.RemoteProductServerMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Random;

public class RemoteProductClientShould {

    private RemoteProductServerMock server;
    private RemoteProductClient client;
    
    @BeforeEach
    public void setUp() {
        server = new RemoteProductServerMock(randomPort());
        server.start();

        client = new RemoteProductClient(server.url());
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test public void
    get_product_details() {
        RemoteProduct productTwo = new RemoteProduct("2", "product2", 10.5, true);

        StepVerifier
            .create(client.product("2"))
            .expectNext(productTwo)
            .verifyComplete();

        server.verifyProductDetails(1, "2");
    }

    @Test public void
    get_similar_product_ids() {
        List<String> similarIds = List.of("2", "3", "4");

        StepVerifier
            .create(client.similarIds("1").collectList())
            .assertNext(
                ids ->
                    Assertions
                        .assertThat(ids)
                        .containsExactlyInAnyOrderElementsOf(similarIds)
            )
            .verifyComplete();

        server.verifySimilarIds(1);
    }

    private int randomPort() {
        return new Random().nextInt(52_000, 53_000);
    }
}