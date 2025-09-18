package com.example.snakesushi.Controller;

import com.example.snakesushi.model.Sushi;
import org.springframework.web.bind.annotation.*;
import com.example.snakesushi.service.SushiService;

import java.util.List;

@RestController
@RequestMapping("/menu")  
public class SushiController {

    private final SushiService service;

    public SushiController(SushiService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sushi> getMenu() {
        return service.findAll();
    }

    @PostMapping
    public Sushi addSushi(@RequestBody Sushi sushi) {
        return service.save(sushi);
    }
}

