package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaComServicosDTO;
import com.carpinaon.dto.categoria.CategoriaRequestDTO;
import com.carpinaon.dto.categoria.CategoriaResponseDTO;
import com.carpinaon.model.Categoria;
import com.carpinaon.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de categorias - consulta e administração das categorias de serviços
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ServicoService servicoService;

    // Lista todas as categorias ativas
    public List<CategoriaResponseDTO> listar() {
        List<Categoria> categorias = categoriaRepository.findByAtivoTrue();
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }

    // Lista as categorias com os serviços dentro (formato da home do app - 1 chamada)
    public List<CategoriaComServicosDTO> listarComServicos() {
        List<Categoria> categorias = categoriaRepository.findByAtivoTrue();
        return categorias.stream()
                .map(this::toComServicos)
                .toList();
    }

    // Busca uma categoria pelo id
    public CategoriaResponseDTO buscar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return toResponse(categoria);
    }

    // Cria uma categoria nova
    public CategoriaResponseDTO criar(CategoriaRequestDTO request) {
        Categoria categoria = new Categoria();
        aplicarRequest(categoria, request);
        return toResponse(categoriaRepository.save(categoria));
    }

    // Atualiza uma categoria existente
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        aplicarRequest(categoria, request);
        return toResponse(categoriaRepository.save(categoria));
    }

    // Deleta categoria (soft delete - esconde sem apagar)
    public void deletar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
    }

    // Copia os campos do request pra entidade
    private void aplicarRequest(Categoria categoria, CategoriaRequestDTO request) {
        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());
        categoria.setIcone(request.getIcone());
        categoria.setCor(request.getCor());
        if (request.getAtivo() != null) {
            categoria.setAtivo(request.getAtivo());
        }
    }

    // Converte entidade pra DTO com serviços embutidos
    private CategoriaComServicosDTO toComServicos(Categoria categoria) {
        return new CategoriaComServicosDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getIcone(),
                categoria.getCor(),
                servicoService.listarPorCategoria(categoria.getId())
        );
    }

    // Converte entidade pra DTO de resposta
    private CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getIcone(),
                categoria.getCor(),
                categoria.getAtivo(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );
    }
}
