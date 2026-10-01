package com.exemplo.filmes.controller;

import com.exemplo.filmes.model.Filme;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final List<Filme> filmes = new ArrayList<>();
    private final AtomicLong contador = new AtomicLong(1);

    public FilmeController() {
        filmes.add(new Filme(contador.getAndIncrement(), "O Senhor dos Anéis: A Sociedade do Anel", "Fantasia", 2001));
        filmes.add(new Filme(contador.getAndIncrement(), "Cidade de Deus", "Drama", 2002));
        filmes.add(new Filme(contador.getAndIncrement(), "Interestelar", "Ficção Científica", 2014));
        filmes.add(new Filme(contador.getAndIncrement(), "Cara de Um, Focinho de Outro", "Animação", 2026));
        filmes.add(new Filme(contador.getAndIncrement(), "O Jogo do Predador", "Ação", 2026));
        filmes.add(new Filme(contador.getAndIncrement(), "O Show de Truman", "Drama", 1998));
    }

    @GetMapping
    public List<Filme> listar() {
        return filmes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable Long id) {
        return filmes.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Filme> cadastrar(@RequestBody Filme filme) {
        filme.setId(contador.getAndIncrement());
        filmes.add(filme);
        return ResponseEntity.status(HttpStatus.CREATED).body(filme);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filme> atualizar(@PathVariable Long id, @RequestBody Filme dados) {
        for (Filme f : filmes) {
            if (f.getId().equals(id)) {
                f.setTitulo(dados.getTitulo());
                f.setGenero(dados.getGenero());
                f.setAnoLancamento(dados.getAnoLancamento());
                return ResponseEntity.ok(f);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean removido = filmes.removeIf(f -> f.getId().equals(id));
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
