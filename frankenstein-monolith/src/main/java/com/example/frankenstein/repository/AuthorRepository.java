package com.example.frankenstein.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.frankenstein.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("select distinct a from Author a left join fetch a.books") //O 'distinct' evita autores duplicados no resultado por causa do JOIN com vários livros.
    List<Author> findAllWithBooks();
}
