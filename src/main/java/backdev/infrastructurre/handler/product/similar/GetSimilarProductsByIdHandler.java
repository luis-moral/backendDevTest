package backdev.infrastructurre.handler.product.similar;

import backdev.domain.product.GetSimilarProductsService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GetSimilarProductsByIdHandler {

    private final GetSimilarProductsService getSimilarProductsService;

    public GetSimilarProductsByIdHandler(GetSimilarProductsService getSimilarProductsService) {
        this.getSimilarProductsService = getSimilarProductsService;
    }

    public Mono<ServerResponse> similar(ServerRequest request) {
        throw new UnsupportedOperationException();
    }
}
