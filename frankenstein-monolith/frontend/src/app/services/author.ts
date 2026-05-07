import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

export interface Author {
  id?: number;
  name: string;
  cpf: string;
  annualIncome: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthorService {
  private apiUrl = 'http://localhost:8080/api/v1/authors';

  // Dados mockados para exibição imediata
  private mockAuthors: Author[] = [
    { id: 1, name: 'Victor Frankenstein', cpf: '123.456.789-00', annualIncome: 50000 },
    { id: 2, name: 'Mary Shelley', cpf: '987.654.321-11', annualIncome: 75000 },
    { id: 3, name: 'Robert Walton', cpf: '456.789.123-22', annualIncome: 30000 }
  ];

  constructor(private http: HttpClient) { }

  getAuthors(): Observable<Author[]> {
    // Retornando os mocks para garantir a visualização no front
    return of(this.mockAuthors);
    // return this.http.get<Author[]>(this.apiUrl); // Comentado por enquanto
  }

  saveAuthor(author: Author): Observable<Author> {
    return this.http.post<Author>(this.apiUrl, author);
  }

  deleteAuthor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
