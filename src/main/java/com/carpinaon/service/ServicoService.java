package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import com.carpinaon.dto.servico.ServicoRequestDTO;
import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.model.Categoria;
import com.carpinaon.model.Servico;
import com.carpinaon.repository.CategoriaRepository;
import com.carpinaon.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Service de serviços - consulta e administração dos serviços do catálogo
@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Lista os serviços ativos de uma categoria
    public List<ServicoResponseDTO> listarPorCategoria(Long categoriaId) {
        List<Servico> servicos = servicoRepository.findByCategoriaIdAndAtivoTrue(categoriaId);
        return servicos.stream()
                .map(this::toResponse)
                .toList();
    }

    // Lista todos os serviços ativos (pra busca geral no app)
    public List<ServicoResponseDTO> listarTodos() {
        List<Servico> servicos = servicoRepository.findByAtivoTrue();
        return servicos.stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca um serviço pelo id
    public ServicoResponseDTO buscar(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        return toResponse(servico);
    }

    // Cria um serviço novo
    public ServicoResponseDTO criar(ServicoRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Servico servico = new Servico(
                categoria,
                request.getNome(),
                request.getDescricao(),
                request.getQuandoUsar(),
                request.getNaoUsarPara(),
                request.getRequerEndereco() != null ? request.getRequerEndereco() : false
        );
        aplicarCamposExtras(servico, request);

        return toResponse(servicoRepository.save(servico));
    }

    // Atualiza um serviço existente
    public ServicoResponseDTO atualizar(Long id, ServicoRequestDTO request) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Só muda a categoria se veio uma diferente
        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            servico.setCategoria(categoria);
        }

        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setQuandoUsar(request.getQuandoUsar());
        servico.setNaoUsarPara(request.getNaoUsarPara());
        if (request.getRequerEndereco() != null) {
            servico.setRequerEndereco(request.getRequerEndereco());
        }
        aplicarCamposExtras(servico, request);

        return toResponse(servicoRepository.save(servico));
    }

    // Deleta serviço (soft delete - esconde sem apagar)
    public void deletar(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        servico.setAtivo(false);
        servicoRepository.save(servico);
    }

    // Copia os campos novos (formType, prazo, documentos, ativo) pra entidade
    private void aplicarCamposExtras(Servico servico, ServicoRequestDTO request) {
        servico.setFormType(request.getFormType());
        servico.setEstimatedDays(request.getEstimatedDays() != null ? request.getEstimatedDays() : 0);
        servico.setRequiredDocuments(request.getRequiredDocuments() != null
                ? new ArrayList<>(request.getRequiredDocuments())
                : new ArrayList<>());
        if (request.getAtivo() != null) {
            servico.setAtivo(request.getAtivo());
        }
    }

    // Converte entidade pra DTO de resposta
    private ServicoResponseDTO toResponse(Servico servico) {
        CategoriaResumidoDTO categoria = new CategoriaResumidoDTO(
                servico.getCategoria().getId(),
                servico.getCategoria().getNome(),
                servico.getCategoria().getIcone(),
                servico.getCategoria().getCor()
        );

        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getQuandoUsar(),
                servico.getNaoUsarPara(),
                servico.getRequerEndereco(),
                servico.getAtivo(),
                servico.getFormType(),
                servico.getEstimatedDays(),
                servico.getRequiredDocuments(),
                categoria,
                servico.getCreatedAt(),
                servico.getUpdatedAt()
        );
    }
}
