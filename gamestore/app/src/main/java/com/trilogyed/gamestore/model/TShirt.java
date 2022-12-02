package com.trilogyed.gamestore.model;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "t_shirt")
public class TShirt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_shirt_id")
    private Integer tShirtId;
    @NotEmpty(message = "T-Shirt size is required")
    private String size;
    @NotEmpty(message = "T-Shirt color is required")
    private String color;
    @NotEmpty(message = "T-Shirt description is required")
    private String description;
    @NotNull(message = "T-Shirt price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Min Price is $0.0")
    @DecimalMax(value = "9999.99", inclusive = true, message = "Max Price is $9999.99")
    private Double price;
    @NotNull(message = "T-Shirt quantity is required")
    @Min(value = 1, message = "Min Quantity 1")
    @Max(value = 50000, message = "Max Quantity is 50,000")
    private Integer quantity;

    public TShirt() {}

    public TShirt(Integer tShirtId, String size, String color, String description, Double price, Integer quantity) {
        this.tShirtId = tShirtId;
        this.size = size;
        this.color = color;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer gettShirtId() {
        return tShirtId;
    }

    public void settShirtId(Integer tShirtId) {
        this.tShirtId = tShirtId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TShirt tShirt = (TShirt) o;
        return Objects.equals(tShirtId, tShirt.tShirtId) && Objects.equals(size, tShirt.size) && Objects.equals(color, tShirt.color) && Objects.equals(description, tShirt.description) && Objects.equals(price, tShirt.price) && Objects.equals(quantity, tShirt.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tShirtId, size, color, description, price, quantity);
    }

    @Override
    public String toString() {
        return "TShirt{" +
                "tShirtId=" + tShirtId +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
