package backdev.infrastructure.repository;

import backdev.domain.product.Product;
import backdev.infrastructure.client.RemoteProduct;

public class RemoteProductRepositoryMapper {

    public Product toProduct(RemoteProduct remoteProduct) {
        return
            new Product(
                remoteProduct.id(),
                remoteProduct.name(),
                remoteProduct.price(),
                remoteProduct.availability()
            );
    }
}
