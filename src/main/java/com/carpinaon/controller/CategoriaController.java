package com.carpinaon.controller;

import com.carpinaon.dto.categoria.CategoriaComServicosDTO;
import com.carpinaon.dto.categoria.CategoriaResponseDTO;
import com.carpinaon.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller de categorias - consulta das categorias de serviços
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // GET /api/v1/categorias
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    // GET /api/v1/categorias/servicos - categorias com serviços dentro (home do app)
    @GetMapping("/servicos")
    public ResponseEntity<List<CategoriaComServicosDTO>> listarComServicos() {
        return ResponseEntity.ok(categoriaService.listarComServicos());
    }

    // GET /api/v1/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscar(id));
    }
}