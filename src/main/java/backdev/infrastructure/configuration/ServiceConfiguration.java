package backdev.infrastructure.configuration;

import backdev.domain.product.ProductRepository;
import backdev.domain.product.SimilarProductsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Value("${service.similar-products.concurrency}")
    private int similarProductsConcurrency;

    @Bean
    public SimilarProductsService similarProductsService(
        ProductRepository productRepository
    ) {
        return new SimilarProductsService(productRepository, similarProductsConcurrency);
    }
}
