# Diário do Dev
Por: Victor Hugo Fedatto | Ciências da Computação, P4, Tarde
---

Olá, professor.

Isso é o que normalmente eu faço nos meus projetos, um arquivo, apelidado de "diário do dev", onde eu coloco algumas observações sobre o código e as coisas que andei mexendo ao longo do projeto.

## Task 01

- Criado o arquivo [data.sql](./target/classes/data.sql) com o povoamento do banco (50 autores e 87 livros) - feito todo a mão, claro ¯\_(ツ)_/¯
- Fiz as alterações em Author.java, AuthorRepository.java e AuthorController.java, corrigindo os erros do N+1 e dos milhares de selects. (Código alterado comentado - boa parte dele).
- Acessei o GET/authors pelo browser, via: "http://localhost:8080/authors" -> Resposta do terminal foi: 

```bash
2026-05-06T20:16:37.956-03:00 DEBUG 47708 --- [frankenstein-monolith] [nio-8080-exec-3] org.hibernate.SQL : select distinct a1_0.id,a1_0.annual_income,b1_0.author_id,b1_0.id,b1_0.title,a1_0.cpf,a1_0.name from author a1_0 left join book b1_0 on a1_0.id=b1_0.author_id
Hibernate: select distinct a1_0.id,a1_0.annual_income,b1_0.author_id,b1_0.id,b1_0.title,a1_0.cpf,a1_0.name from author a1_0 left join book b1_0 on a1_0.id=b1_0.author_id
```
---

## Task 02

