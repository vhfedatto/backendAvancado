package com.example.frankenstein.adapter.out.persistence;

import com.example.frankenstein.application.port.out.AuthorPersistencePort;
import com.example.frankenstein.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorPersistenceAdapter implements AuthorPersistencePort {

    // Este adapter é a ponte entre a porta de saída e o JPA do Spring.
    private final AuthorJpaRepository authorJpaRepository;

    public AuthorPersistenceAdapter(AuthorJpaRepository authorJpaRepository) {
        this.authorJpaRepository = authorJpaRepository;
    }

    @Override
    public List<Author> findAllWithBooks() {
        return authorJpaRepository.findAllWithBooks();
    }

    @Override
    public Author save(Author author) {
        return authorJpaRepository.save(author);
    }

    @Override
    public void deleteById(Long id) {
        authorJpaRepository.deleteById(id);
    }
}
