package backdev.infrastructure.handler.product.similar;

import backdev.domain.product.Product;
import backdev.infrastructure.util.validator.RequestParameterValidator;
import org.springframework.web.reactive.function.server.ServerRequest;

public class GetSimilarProductsHandlerMapper {

    private final RequestParameterValidator validator;

    public GetSimilarProductsHandlerMapper(RequestParameterValidator validator) {
        this.validator = validator;
    }

    public String toProductId(ServerRequest request) {
        return validator.mandatoryString(request.pathVariable("product_id"), "productId");
    }

    public GetSimilarProductsItemResponse toGetSimilarProductsItemResponse(Product product) {
        return
            new GetSimilarProductsItemResponse(
                product.id(),
                product.name(),
                product.price(),
                product.availability()
            );
    }
}
