package backdev.infrastructure.configuration;

import backdev.domain.product.SimilarProductsService;
import backdev.infrastructure.handler.product.similar.GetSimilarProductsHandler;
import backdev.infrastructure.handler.product.similar.GetSimilarProductsHandlerMapper;
import backdev.infrastructure.handler.status.get.GetStatusHandler;
import backdev.infrastructure.util.validator.RequestParameterValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public GetStatusHandler getStatusHandler() {
        return new GetStatusHandler();
    }

    @Bean
    public GetSimilarProductsHandler getSimilarProductsHandler(
        SimilarProductsService similarProductsService,
        RequestParameterValidator requestParameterValidator
    ) {
        return
            new GetSimilarProductsHandler(
                similarProductsService,
                new GetSimilarProductsHandlerMapper(requestParameterValidator)
            );
    }
}
