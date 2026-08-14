package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.model.Servico;
import com.carpinaon.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service de serviços - lista os serviços do catálogo pro app
@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

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
                categoria
        );
    }
}