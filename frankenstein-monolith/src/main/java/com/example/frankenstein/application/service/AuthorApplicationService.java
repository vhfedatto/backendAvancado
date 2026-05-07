package com.example.frankenstein.application.service;

import com.example.frankenstein.application.port.in.AuthorUseCase;
import com.example.frankenstein.application.port.out.AuthorPersistencePort;
import com.example.frankenstein.domain.service.CpfValidator;
import com.example.frankenstein.domain.service.IncomeTaxCalculator;
import com.example.frankenstein.dto.AuthorRequest;
import com.example.frankenstein.dto.AuthorResponse;
import com.example.frankenstein.dto.BookResponse;
import com.example.frankenstein.model.Author;
import com.example.frankenstein.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorApplicationService implements AuthorUseCase {

    // Aqui ficou o caso de uso da aplicação: ele orquestra domínio + persistência.
    private final AuthorPersistencePort authorPersistencePort;
    private final CpfValidator cpfValidator;
    private final IncomeTaxCalculator incomeTaxCalculator;

    public AuthorApplicationService(
        AuthorPersistencePort authorPersistencePort,
        CpfValidator cpfValidator,
        IncomeTaxCalculator incomeTaxCalculator
    ) {
        this.authorPersistencePort = authorPersistencePort;
        this.cpfValidator = cpfValidator;
        this.incomeTaxCalculator = incomeTaxCalculator;
    }

    @Override
    public List<AuthorResponse> listAll() {
        return authorPersistencePort.findAllWithBooks()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public AuthorResponse create(AuthorRequest request) {
        cpfValidator.validate(request.cpf());

        Author author = new Author();
        author.setName(request.name());
        author.setCpf(request.cpf());
        author.setAnnualIncome(incomeTaxCalculator.applyBusinessRule(request.annualIncome()));
        author.setBooks(toBooks(request, author));

        Author savedAuthor = authorPersistencePort.save(author);
        return toResponse(savedAuthor);
    }

    @Override
    public void delete(Long id) {
        authorPersistencePort.deleteById(id);
    }

    private List<Book> toBooks(AuthorRequest request, Author author) {
        // A conversão entre DTO e entidade ficou aqui porque faz parte do caso de uso.
        if (request.books() == null || request.books().isEmpty()) {
            return new ArrayList<>();
        }

        return request.books()
            .stream()
            .map(bookRequest -> {
                Book book = new Book();
                book.setTitle(bookRequest.title());
                book.setAuthor(author);
                return book;
            })
            .toList();
    }

    private AuthorResponse toResponse(Author author) {
        // A saída da aplicação continua em DTO, sem expor entidade JPA na API.
        List<BookResponse> books = author.getBooks() == null
            ? List.of()
            : author.getBooks()
                .stream()
                .map(book -> new BookResponse(book.getId(), book.getTitle()))
                .toList();

        return new AuthorResponse(
            author.getId(),
            author.getName(),
            author.getCpf(),
            author.getAnnualIncome(),
            books
        );
    }
}
