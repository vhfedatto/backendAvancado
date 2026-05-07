import { Component, OnInit } from '@angular/core';
import { BookService, Book } from '../../services/book';
import { CommonModule } from '@angular/common';


@Component({
    selector: 'app-book-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './book-list.html',
    styleUrl: './book-list.css'
})
export class BookListComponent implements OnInit {
    books: Book[] = [];
    errorMessage: string = "";
    // Controla a troca entre skeleton, lista carregada e mensagem de erro.
    isLoading = true;

    constructor(private bookService: BookService) { }

    ngOnInit(): void {
        this.bookService.getBooks().subscribe({
            next: (data) => {
                // Quando a API responde, o componente troca os placeholders pelos cards reais.
                this.books = data;
                this.errorMessage = "";
                this.isLoading = false;
            },
            error: (err) => {
                this.errorMessage = "Could not connect to the backend API. Please check if the Spring Boot server is running.";
                this.isLoading = false;
                console.error('API Error:', err);
            }
        });
    }
}
