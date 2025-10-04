package com.example.snakesushi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sushi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String seasoning;
    private String imagePath;
    private String type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sushi sushi = (Sushi) o;
        return Objects.equals(id, sushi.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

