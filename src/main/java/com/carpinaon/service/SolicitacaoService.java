package com.carpinaon.service;

import com.carpinaon.dto.solicitacao.SolicitacaoRequestDTO;
import com.carpinaon.model.Servico;
import com.carpinaon.model.Solicitacao;
import com.carpinaon.model.Usuario;
import com.carpinaon.model.enums.StatusSolicitacao;
import com.carpinaon.repository.ServicoRepository;
import com.carpinaon.repository.SolicitacaoRepository;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

// Service de solicitações - criar e acompanhar protocolos
@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    // Criar nova solicitação
    public Solicitacao criar(Long usuarioId, SolicitacaoRequestDTO request) {
        // Validação: só pode selecionar 1 serviço
        if (request.getServicoIds().size() > 1) {
            throw new RuntimeException("Só é possível selecionar 1 serviço por solicitação");
        }

        // Busca o usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca o serviço
        Long servicoId = request.getServicoIds().get(0);
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Gera protocolo único (ANO-XXXX)
        String protocolo = gerarProtocolo();

        // Cria a solicitação
        Solicitacao solicitacao = new Solicitacao(
                usuario,
                servico,
                protocolo,
                request.getCep(),
                null, // endereço será preenchido pelo ViaCEP depois
                request.getComplemento(),
                request.getPontoReferencia(),
                request.getDescricao()
        );

        return solicitacaoRepository.save(solicitacao);
    }

    // Buscar solicitação por protocolo
    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacaoRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    // Listar solicitações do usuário
    public List<Solicitacao> listarPorUsuario(Long usuarioId) {
        return solicitacaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    // Gerar protocolo único (ANO-XXXX)
    private String gerarProtocolo() {
        String ano = String.valueOf(Year.now().getValue());
        String codigo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return ano + "-" + codigo;
    }
}
