import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Author } from './author';

export interface Book {
    id?: number;
    title: string;
    author: Author;
}

@Injectable({
    providedIn: 'root'
})
export class BookService {
    // Este contrato precisa acompanhar o endpoint Spring exposto em /api/v1/books.
    private apiUrl = 'http://localhost:8080/api/v1/books';

    constructor(private http: HttpClient) { }

    getBooks(): Observable<Book[]> {
        return this.http.get<Book[]>(this.apiUrl);
    }

    saveBook(book: Book): Observable<Book> {
        return this.http.post<Book>(this.apiUrl, book);
    }

    deleteBook(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
