package backdev.infrastructurre.handler.product.similar;

import backdev.domain.product.Product;
import backdev.infrastructurre.util.validator.RequestParameterValidator;
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
        throw new UnsupportedOperationException();
    }
}
