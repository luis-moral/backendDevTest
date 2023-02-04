package backdev.infrastructurre.configuration;

import backdev.domain.product.GetSimilarProductsService;
import backdev.infrastructurre.handler.product.similar.GetSimilarProductsByIdHandler;
import backdev.infrastructurre.handler.status.get.GetStatusHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public GetStatusHandler getStatusHandler() {
        return new GetStatusHandler();
    }

    @Bean
    public GetSimilarProductsByIdHandler getSimilarProductsByIdHandler(
        GetSimilarProductsService getSimilarProductsService
    ) {
        return new GetSimilarProductsByIdHandler(getSimilarProductsService);
    }
}
