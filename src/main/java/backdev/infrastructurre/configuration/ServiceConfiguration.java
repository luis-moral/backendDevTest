package backdev.infrastructurre.configuration;

import backdev.domain.product.GetSimilarProductsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public GetSimilarProductsService getSimilarProductsService() {
        return new GetSimilarProductsService();
    }
}
