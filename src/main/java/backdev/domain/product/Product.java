package backdev.domain.product;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Product {

    private final String id;
    private final String name;
    private final double price;
    private final boolean availability;

    public Product(String id, String name, double price, boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public double price() {
        return price;
    }

    public boolean availability() {
        return availability;
    }
}
