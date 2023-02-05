package backdev.infrastructure.client;

public record RemoteProduct(
    String id,
    String name,
    double price,
    boolean availability
) {}
