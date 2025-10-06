package com.example.snakesushi.service;

import com.example.snakesushi.Repository.SushiRepository;
import com.example.snakesushi.model.Sushi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SushiService {

    private final SushiRepository repository;

    public List<Sushi> findAll() {
        return repository.findAll();
    }
}
