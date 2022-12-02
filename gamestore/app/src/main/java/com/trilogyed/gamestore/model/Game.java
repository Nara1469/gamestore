package com.trilogyed.gamestore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Integer gameId;
    @NotEmpty(message = "Title of the game is required.")
    private String title;
    @NotEmpty(message = "ESRB Rating of the game is required.")

    @Column(name = "esrb_rating")
    private String esrbRating;
    @NotEmpty(message = "Description of the game is required.")
    private String description;
    @NotEmpty(message = "Studio of the game is required.")
    private String studio;
    @NotNull(message = "Price of the game is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Min Price for this item is $0.0")
    @DecimalMax(value = "9999.99", inclusive = true, message = "Max Price is $9999.99")
    private Double price;
    @NotNull(message = "Game quantity is required")
    @Min(value = 1, message = "Min Quantity is 1")
    @Max(value = 50000, message = "Max Quantity is 50,000")
    private Integer quantity;

    public Game() {}

    public Game(Integer gameId, String title, String esrbRating, String description, String studio, Double price, Integer quantity) {
        this.gameId = gameId;
        this.title = title;
        this.esrbRating = esrbRating;
        this.description = description;
        this.studio = studio;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
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
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId) && Objects.equals(title, game.title) && Objects.equals(esrbRating, game.esrbRating) && Objects.equals(description, game.description) && Objects.equals(studio, game.studio) && Objects.equals(price, game.price) && Objects.equals(quantity, game.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, esrbRating, description, studio, price, quantity);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", esrbRating='" + esrbRating + '\'' +
                ", description='" + description + '\'' +
                ", studio='" + studio + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
