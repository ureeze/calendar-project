package org.example;


import lombok.RequiredArgsConstructor;
import org.example.core.SimpleEntity;
import org.example.core.SimpleEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableJpaAuditing
@RestController
@RequiredArgsConstructor
@SpringBootApplication
public class Main {

    private final SimpleEntityRepository repository;


    @GetMapping
    public List<SimpleEntity> findAll() {
        return repository.findAll();
    }

    @PostMapping("/save")
    public SimpleEntity saveOne() {
        final SimpleEntity e = new SimpleEntity();
        e.setName("hello");
        return repository.save(e);
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}