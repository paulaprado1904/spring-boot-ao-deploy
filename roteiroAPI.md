### Passo a Passo para Implementar uma API em Java Spring

1. **Criar o Projeto Spring Boot:**
   - Utilize o Spring Initializr (ou outra ferramenta de geração de projetos) para configurar o projeto Spring Boot. Escolha as dependências essenciais, como Spring Web, Spring Data JPA, e o banco de dados que será utilizado (ex.: MySQL, H2, PostgreSQL).

2. **Configurar o Arquivo `application.properties` ou `application.yml`:**
   - Defina as configurações do banco de dados, portas, e outras propriedades do projeto. Exemplo:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
     spring.datasource.username=usuario
     spring.datasource.password=senha
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Criar o Package de Models/Entities:**
   - **Papel:** Representa as entidades do banco de dados. Cada classe no package `models` (ou `entities`) corresponde a uma tabela do banco.
   - **Exemplo:** Criar uma classe `Usuario` com anotações como `@Entity`, `@Table`, e atributos mapeados para colunas do banco.

4. **Criar o Package de Repositories:**
   - **Papel:** A camada de acesso a dados, responsável por interagir com o banco. Estende `JpaRepository` ou `CrudRepository`.
   - **Exemplo:** Criar uma interface `UsuarioRepository` que estenda `JpaRepository<Usuario, Long>`.

5. **Criar o Package de Services:**
   - **Papel:** Contém a lógica de negócios da aplicação. Os services interagem com os repositories e podem realizar validações, transformações de dados, entre outras operações antes de repassar a informação para os controllers.
   - **Exemplo:** Criar uma classe `UsuarioService` que injeta `UsuarioRepository` para realizar operações de CRUD sobre a entidade `Usuario`.

6. **Criar o Package de Controllers:**
   - **Papel:** Expõe os endpoints da API. Os controllers recebem as requisições HTTP, interagem com os services, e retornam respostas para o cliente.
   - **Exemplo:** Criar uma classe `UsuarioController` com endpoints como `@GetMapping`, `@PostMapping`, `@PutMapping`, etc., para lidar com operações de CRUD.

7. **Implementar Validações e Tratamento de Exceções:**
   - **Papel:** Garantir que os dados enviados para a API sejam válidos e que erros sejam tratados adequadamente.
   - **Exemplo:** Usar anotações como `@Valid`, `@NotNull`, `@Size` em DTOs ou entities. Criar um `GlobalExceptionHandler` usando `@ControllerAdvice` para capturar e tratar exceções.

8. **Criar Testes Unitários e de Integração:**
   - **Papel:** Verificar se as partes individuais (testes unitários) e a aplicação como um todo (testes de integração) funcionam como esperado.
   - **Exemplo:** Usar `JUnit` e `MockMvc` para criar testes que validem o comportamento dos controllers e services.

9. **Documentar a API:**
   - **Papel:** Gerar a documentação dos endpoints da API para facilitar o entendimento e uso por outras equipes.
   - **Exemplo:** Usar `Swagger` (ou Springdoc OpenAPI) para documentar automaticamente os endpoints da API.

10. **Deploy da Aplicação:**
    - **Papel:** Colocar a aplicação em um ambiente de produção.
    - **Exemplo:** Configurar o deploy no servidor de sua escolha (como AWS, Heroku, ou DigitalOcean), configurando variáveis de ambiente e serviços necessários.


---

## Exemplo Estrutural


### Estrutura do Projeto

```
src/main/java/com/exemplo/projeto
│
├── controller
│   └── UsuarioController.java
│
├── model
│   └── Usuario.java
│
├── repository
│   └── UsuarioRepository.java
│
└── service
    └── UsuarioService.java
```

### 1. Configuração do Projeto

No arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/exemplo_db
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### 2. Entidade `Usuario`

**Arquivo:** `src/main/java/com/exemplo/projeto/model/Usuario.java`

```java
package com.exemplo.projeto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

### 3. Repositório `UsuarioRepository`

**Arquivo:** `src/main/java/com/exemplo/projeto/repository/UsuarioRepository.java`

```java
package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Encontrar usuários pelo nome
    List<Usuario> findByNome(String nome);
    
    // Encontrar usuários pelo email
    Optional<Usuario> findByEmail(String email);
    
    // Verificar a existência de um usuário pelo email
    boolean existsByEmail(String email);
    
    // Encontrar usuários cujo nome contém uma substring
    List<Usuario> findByNomeContaining(String substring);
    
    // Encontrar usuários que foram criados após uma data específica
    // List<Usuario> findByDataCriacaoAfter(LocalDate data);
}

```

### 4. Serviço `UsuarioService`

**Arquivo:** `src/main/java/com/exemplo/projeto/service/UsuarioService.java`

```java
package com.exemplo.projeto.service;

import com.exemplo.projeto.model.Usuario;
import com.exemplo.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
```

### 5. Controlador `UsuarioController`

**Arquivo:** `src/main/java/com/exemplo/projeto/controller/UsuarioController.java`

```java
package com.exemplo.projeto.controller;

import com.exemplo.projeto.model.Usuario;
import com.exemplo.projeto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obterUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.salvarUsuario(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioExistente = usuarioService.obterUsuarioPorId(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            return ResponseEntity.ok(usuarioService.salvarUsuario(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (usuarioService.obterUsuarioPorId(id).isPresent()) {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```
