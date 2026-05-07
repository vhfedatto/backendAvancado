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

- Implementei autenticação com JWT, com login em `POST /auth/login`, filtro para ler `Authorization: Bearer <token>` e validação do usuário pelo banco H2.
- Removi o usuário hardcoded em memória e passei a usar `app_user` no banco, com senha em hash BCrypt e roles `ADMIN` / `USER`.
- Corrigi o `JwtService` para usar a chave do `application.properties` como texto puro. Antes disso, o login quebrava porque o código tentava decodificar a chave como Base64.
- Ajustei a segurança para deixar `POST /auth/login` público, exigir autenticação no restante da API e restringir `DELETE /authors/{id}` para `ADMIN`.
- Validação prática dos testes:

```bash
POST /auth/login com admin/admin123 -> 200 OK + token
DELETE /authors/1 sem token -> 403/401 esperado pelo fluxo de segurança
DELETE /authors/1 com token de user -> 403 Forbidden
DELETE /authors/1 com token de admin -> 200 OK
```

- Observação prática: para o teste no PowerShell funcionar, precisei salvar o token em variável antes de mandar no header `Authorization`.

## Task 03