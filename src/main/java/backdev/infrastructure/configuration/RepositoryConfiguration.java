package backdev.infrastructure.configuration;

import backdev.domain.product.ProductRepository;
import backdev.infrastructure.client.RemoteProductClient;
import backdev.infrastructure.repository.RemoteProductRepository;
import backdev.infrastructure.repository.RemoteProductRepositoryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public ProductRepository productRepository(RemoteProductClient remoteProductClient) {
        return new RemoteProductRepository(remoteProductClient, new RemoteProductRepositoryMapper());
    }
}
