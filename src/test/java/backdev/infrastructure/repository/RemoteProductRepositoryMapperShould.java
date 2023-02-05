package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.infrastructure.client.RemoteProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoteProductRepositoryMapperShould {

    private RemoteProductRepositoryMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new RemoteProductRepositoryMapper();
    }

    @Test public void
    map_remote_product_to_product() {
        RemoteProduct remoteProduct = new RemoteProduct("1", "product1", 10.0, true);
        Product expected = new Product("1", "product1", 10.0, true);

        Assertions
            .assertThat(mapper.toProduct(remoteProduct))
            .isEqualTo(expected);
    }
}