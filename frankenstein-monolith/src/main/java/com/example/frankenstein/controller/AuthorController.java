package com.example.frankenstein.controller;

import com.example.frankenstein.model.Author;
import com.example.frankenstein.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository repository;

    @GetMapping
    public List<Author> listAll() {
        return repository.findAllWithBooks(); 
    }

    @PostMapping
    public Author save(@RequestBody Author author) {
        if (author.getCpf() == null || author.getCpf().length() != 11) {
            throw new RuntimeException("CPF Inválido!");
        }
        if (author.getAnnualIncome() > 50000) {
            author.setAnnualIncome(author.getAnnualIncome() * 0.85);
        } else {
            author.setAnnualIncome(author.getAnnualIncome() * 0.93);
        }

        return repository.save(author);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
