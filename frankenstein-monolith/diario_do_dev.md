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

---

## Task 03

- Refatorei o `AuthorController` para ele parar de carregar regra de negócio. Agora ele só recebe a requisição HTTP e delega para `AuthorService`.
- Criei a camada de service com `AuthorService`, além das classes `CpfValidator` e `IncomeTaxCalculator`, para aplicar melhor o SRP e tirar validação/cálculo de dentro do controller.
- Passei a usar DTOs com `record`: `AuthorRequest`, `AuthorResponse`, `BookRequest` e `BookResponse`. Com isso, a API parou de expor a entidade JPA `Author` diretamente.

- No `AuthorService`, centralizei:
  - validação de CPF
  - cálculo da renda ajustada
  - conversão entre DTO e entidade
  - associação dos livros com o autor antes do save

- Também deixei comentários curtos no código para mostrar o raciocínio e facilitar a leitura rápida.

- Depois aprofundei um pouco mais a ideia de hexagonal no fluxo de autores:
  - `adapter.in.web.AuthorController` virou o adaptador de entrada HTTP
  - `application.port.in.AuthorUseCase` virou a porta de entrada
  - `application.service.AuthorApplicationService` virou o caso de uso
  - `application.port.out.AuthorPersistencePort` virou a porta de saída
  - `adapter.out.persistence.AuthorPersistenceAdapter` + `AuthorJpaRepository` viraram o adaptador de persistência
  - `domain.service.CpfValidator` e `domain.service.IncomeTaxCalculator` ficaram mais claramente no lado da regra de negócio
- Ou seja: não ficou uma hexagonal "gigante", mas a separação entre entrada, aplicação, domínio e saída ficou bem mais explícita.

---

## Integração Front-End: Books

- Como o front novo tinha uma tela `/books`, faltava um endpoint real no backend para essa tela deixar de quebrar no navegador.
- Implementei a rota `GET /api/v1/books` no backend, seguindo o mesmo espírito da separação hexagonal já aplicada em autores:
  - `application.port.in.BookUseCase`
  - `application.service.BookApplicationService`
  - `application.port.out.BookPersistencePort`
  - `adapter.out.persistence.BookPersistenceAdapter`
  - `adapter.out.persistence.BookJpaRepository`
  - `adapter.in.web.BookController`
- Mantive a resposta enxuta com `BookResponse(id, title)`, porque era isso que a tela de livros precisava para renderizar.
- Ajustei a segurança para liberar `GET /api/v1/books` sem JWT, já que o front ainda não tem fluxo de login integrado para navegar na listagem.
- Também ativei CORS para `http://localhost:4200`, senão o browser bloquearia a chamada mesmo com o endpoint funcionando no backend.
- No Angular
  - mantive `BookService` apontando para `http://localhost:8080/api/v1/books`
  - ajustei o `BookListComponent` para trocar o estado de loading pela lista real de livros quando a API responde
  - a tela de livros deixou de ficar presa só nos skeletons/placeholders

- Validação prática:

```bash
mvn test
npm run build
```

- O teste de integração novo garante o contrato mínimo da tela:
  - `GET /api/v1/books` responde `200 OK`
  - o payload traz ao menos `id` e `title`

- Observação importante:
  - a tela de autores ainda está usando mock local no front
  - então a integração completa front-back ainda não está 100% fechada em autores
  - mas a navegação e a listagem de livros agora passam por backend real
