package backdev.infrastructure.handler.product.similar;

import backdev.domain.product.SimilarProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public class GetSimilarProductsHandler {

    private final SimilarProductsService similarProductsService;
    private final GetSimilarProductsHandlerMapper mapper;

    public GetSimilarProductsHandler(
        SimilarProductsService similarProductsService,
        GetSimilarProductsHandlerMapper mapper
    ) {
        this.similarProductsService = similarProductsService;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> similar(ServerRequest request) {
        return
            ServerResponse
                .status(HttpStatus.OK)
                .body(
                    similarProductsService
                        .findProducts(mapper.toProductId(request))
                        .map(mapper::toGetSimilarProductsItemResponse)
                        .collectList(),
                        List.class
                );
    }
}
