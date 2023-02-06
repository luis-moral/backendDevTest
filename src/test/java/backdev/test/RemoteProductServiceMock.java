package backdev.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import wiremock.org.apache.hc.core5.http.ContentType;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class RemoteProductServiceMock {

    private int port;
    private WireMockServer mockServer;

    public RemoteProductServiceMock(int port) {
        this.port = port;
    }

    public void start() {
        mockServer = new WireMockServer(port);
        mockServer.start();

        mockProductDetailsEndpoint();
        mockSimilarIdsEndpoint();
    }

    public String url() {
        return mockServer.baseUrl();
    }

    public void stop() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.stop();
        }
    }

    public void verifyProductDetails(int amount, String productId) {
        mockServer
            .verify(
                amount,
                getRequestedFor(urlPathEqualTo("/product/" + productId))
            );
    }

    public void verifySimilarIds(int amount, String productId) {
        mockServer
            .verify(
                amount,
                getRequestedFor(urlPathEqualTo("/product/" + productId + "/similarids"))
            );
    }

    private void mockProductDetailsEndpoint() {
        IntStream
            .rangeClosed(2, 5)
            .forEach(
                id ->
                    mockServer
                        .stubFor(
                            get(urlPathEqualTo("/product/" + id))
                                .willReturn(
                                    aResponse()
                                        .withStatus(200)
                                        .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.getMimeType())
                                        .withBody(TestUtils.readFile("mock/remote-product/product_" + id + ".json"))
                                )
                        )
            );
    }

    private void mockSimilarIdsEndpoint() {
        mockServer
            .stubFor(
                get(urlPathEqualTo("/product/1/similarids"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.getMimeType())
                            .withBody("[2, 3, 4]")
                    )
            );

        mockServer
            .stubFor(
                get(urlPathEqualTo("/product/100/similarids"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withHeader(ContentTypeHeader.KEY, ContentType.APPLICATION_JSON.getMimeType())
                            .withBody("[5]")
                    )
            );
    }
}
