package com.example.snakesushi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
@Entity
@Table(name = "menu")
@Data
public class Sushi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String seasoning;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sushi sushi = (Sushi) o;
        return Double.compare(price, sushi.price) == 0 && Objects.equals(id, sushi.id) && Objects.equals(name, sushi.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}

