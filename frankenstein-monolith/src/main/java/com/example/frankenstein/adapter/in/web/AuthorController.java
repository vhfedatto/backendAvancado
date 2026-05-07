package com.example.frankenstein.adapter.in.web;

import com.example.frankenstein.application.port.in.AuthorUseCase;
import com.example.frankenstein.dto.AuthorRequest;
import com.example.frankenstein.dto.AuthorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    // O controller agora depende de uma porta de entrada, não da implementação concreta do caso de uso.
    private final AuthorUseCase authorUseCase;

    public AuthorController(AuthorUseCase authorUseCase) {
        this.authorUseCase = authorUseCase;
    }

    @GetMapping
    public List<AuthorResponse> listAll() {
        return authorUseCase.listAll();
    }

    @PostMapping
    public AuthorResponse save(@RequestBody AuthorRequest request) {
        return authorUseCase.create(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        authorUseCase.delete(id);
    }
}
