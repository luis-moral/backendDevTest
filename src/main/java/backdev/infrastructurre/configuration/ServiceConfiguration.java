package backdev.infrastructurre.configuration;

import backdev.domain.product.SimilarProductsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public SimilarProductsService similarProductsService() {
        return new SimilarProductsService();
    }
}
