package backdev.infrastructure.handler.product.similar;

import backdev.domain.product.Product;
import backdev.infrastructure.util.validator.RequestParameterValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

public class GetSimilarProductsHandlerMapperShould {

    private GetSimilarProductsHandlerMapper mapper;
    private RequestParameterValidator validator;

    @BeforeEach
    public void setUp() {
        validator = Mockito.mock(RequestParameterValidator.class);

        mapper = new GetSimilarProductsHandlerMapper(validator);
    }

    @Test public void
    map_product_id_path_parameter_to_product_id() {
        ServerRequest request =
            MockServerRequest
                .builder()
                .pathVariable("product_id", "5")
                .build();

        Mockito
            .when(validator.mandatoryString("5", "productId"))
            .thenReturn("5");

        Assertions
            .assertThat(mapper.toProductId(request))
            .isEqualTo("5");

        Mockito
            .verify(validator, Mockito.times(1))
            .mandatoryString(Mockito.any(), Mockito.any());
    }

    @Test public void
    map_product_to_similar_product_response() {
        Product productOne = new Product("1", "product1", 10.25, true);
        GetSimilarProductsItemResponse expected = new GetSimilarProductsItemResponse("1", "product1", 10.25, true);

        Assertions
            .assertThat(mapper.toGetSimilarProductsItemResponse(productOne))
            .isEqualTo(expected);
    }
}