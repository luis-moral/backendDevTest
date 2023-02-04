package backdev.infrastructurre.handler.product.similar;

public record GetSimilarProductsItemResponse(
    String id,
    String name,
    double price,
    boolean availability
) {}
