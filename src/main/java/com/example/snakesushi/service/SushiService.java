package com.example.snakesushi.service;

import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Sushi;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SushiService {
    private final SushiRepository repository;

    public SushiService(SushiRepository repository) {
        this.repository = repository;
    }

    public List<Sushi> findAll() {
        return repository.findAll();
    }

}

