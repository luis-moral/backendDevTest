package backdev.infrastructure.configuration;

import backdev.domain.product.ProductRepository;
import backdev.infrastructure.repository.RemoteProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public ProductRepository productRepository() {
        return new RemoteProductRepository();
    }
}
