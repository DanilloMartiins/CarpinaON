package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaResponseDTO;
import com.carpinaon.model.Categoria;
import com.carpinaon.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de categorias - lista as categorias de serviços pro app
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Lista todas as categorias (na ordem que for mais útil pro app)
    public List<CategoriaResponseDTO> listar() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca uma categoria pelo id
    public CategoriaResponseDTO buscar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return toResponse(categoria);
    }

    // Converte entidade pra DTO de resposta
    private CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getIcone(),
                categoria.getCor()
        );
    }
}