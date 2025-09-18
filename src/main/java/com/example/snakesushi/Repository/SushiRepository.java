package com.example.snakesushi.Repository;

import com.example.snakesushi.model.Sushi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SushiRepository extends JpaRepository<Sushi, Long> {
}
