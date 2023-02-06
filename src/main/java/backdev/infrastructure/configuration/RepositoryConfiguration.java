package backdev.infrastructure.configuration;

import backdev.domain.product.ProductRepository;
import backdev.infrastructure.client.RemoteProductClient;
import backdev.infrastructure.repository.RemoteProductRepository;
import backdev.infrastructure.repository.RemoteProductRepositoryMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Value("${repository.product.cache.duration}")
    private long cacheDuration;

    @Bean
    public ProductRepository productRepository(
        RemoteProductClient remoteProductClient
    ) {
        return
            new RemoteProductRepository(
                remoteProductClient,
                cacheDuration,
                new RemoteProductRepositoryMapper()
            );
    }
}
