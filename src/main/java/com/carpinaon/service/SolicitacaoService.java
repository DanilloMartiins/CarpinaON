package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import com.carpinaon.dto.servico.ServicoResumidoDTO;
import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import com.carpinaon.dto.solicitacao.AnexoResponseDTO;
import com.carpinaon.dto.solicitacao.HistoricoStatusResponseDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoRequestDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResumidoDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResponseDTO;
import com.carpinaon.model.Anexo;
import com.carpinaon.model.HistoricoStatus;
import com.carpinaon.model.Servico;
import com.carpinaon.model.Solicitacao;
import com.carpinaon.model.Usuario;
import com.carpinaon.model.enums.StatusSolicitacao;
import com.carpinaon.repository.AnexoRepository;
import com.carpinaon.repository.HistoricoStatusRepository;
import com.carpinaon.repository.ServicoRepository;
import com.carpinaon.repository.SolicitacaoRepository;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    // Criar nova solicitação
    @Transactional
    public SolicitacaoResponseDTO criar(Long usuarioId, SolicitacaoRequestDTO request) {
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

        solicitacao = solicitacaoRepository.save(solicitacao);

        // Registra o primeiro histórico de status
        historicoStatusRepository.save(new HistoricoStatus(
                solicitacao,
                null,
                StatusSolicitacao.RECEBIDA,
                "Solicitação criada pelo cidadão",
                usuarioId
        ));

        return toResponse(solicitacao);
    }

    // Buscar solicitação por protocolo
    public SolicitacaoResponseDTO buscarPorProtocolo(String protocolo) {
        Solicitacao solicitacao = solicitacaoRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
        return toResponse(solicitacao);
    }

    // Listar solicitações do usuário (resumido, sem anexos/histórico)
    public List<SolicitacaoResumidoDTO> listarPorUsuario(Long usuarioId) {
        List<Solicitacao> solicitacoes = solicitacaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
        return solicitacoes.stream()
                .map(this::toResumido)
                .toList();
    }

    // Atualizar status da solicitação (admin)
    @Transactional
    public SolicitacaoResponseDTO atualizarStatus(Long id, StatusSolicitacao novoStatus,
                                                   String observacao, Long changedBy) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        StatusSolicitacao statusAnterior = solicitacao.getStatus();

        // Se for indeferir, exige observação (padrão de boa prática)
        if (novoStatus == StatusSolicitacao.INDEFERIDA && (observacao == null || observacao.isBlank())) {
            throw new RuntimeException("Observação é obrigatória para indeferir uma solicitação");
        }

        solicitacao.setStatus(novoStatus);
        solicitacaoRepository.save(solicitacao);

        historicoStatusRepository.save(new HistoricoStatus(
                solicitacao,
                statusAnterior,
                novoStatus,
                observacao,
                changedBy
        ));

        return toResponse(solicitacao);
    }

    // Converte entidade pra DTO resumido (listagem)
    private SolicitacaoResumidoDTO toResumido(Solicitacao solicitacao) {
        Servico servico = solicitacao.getServico();
        CategoriaResumidoDTO categoria = new CategoriaResumidoDTO(
                servico.getCategoria().getId(),
                servico.getCategoria().getNome(),
                servico.getCategoria().getIcone(),
                servico.getCategoria().getCor()
        );
        ServicoResumidoDTO servicoResumido = new ServicoResumidoDTO(
                servico.getId(),
                servico.getNome(),
                categoria
        );

        return new SolicitacaoResumidoDTO(
                solicitacao.getId(),
                solicitacao.getProtocolo(),
                solicitacao.getStatus(),
                solicitacao.getStatus().getDescricao(),
                servicoResumido,
                solicitacao.getDescricao(),
                solicitacao.getEndereco(),
                solicitacao.getCreatedAt()
        );
    }

    // Converte entidade pra DTO completo (detalhe)
    private SolicitacaoResponseDTO toResponse(Solicitacao solicitacao) {
        List<AnexoResponseDTO> anexos = anexoRepository.findBySolicitacaoId(solicitacao.getId())
                .stream()
                .map(this::toAnexoResponse)
                .toList();

        List<HistoricoStatusResponseDTO> historico = historicoStatusRepository
                .findBySolicitacaoIdOrderByChangedAtDesc(solicitacao.getId())
                .stream()
                .map(this::toHistoricoResponse)
                .toList();

        Usuario usuario = solicitacao.getUsuario();
        Servico servico = solicitacao.getServico();

        UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getNumeroCNS(),
                usuario.getRg(),
                usuario.getNis(),
                usuario.getStatusVerificacao(),
                usuario.getRole(),
                usuario.getCreatedAt()
        );

        CategoriaResumidoDTO categoria = new CategoriaResumidoDTO(
                servico.getCategoria().getId(),
                servico.getCategoria().getNome(),
                servico.getCategoria().getIcone(),
                servico.getCategoria().getCor()
        );

        ServicoResponseDTO servicoDTO = new ServicoResponseDTO(
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
                categoria
        );

        return new SolicitacaoResponseDTO(
                solicitacao.getId(),
                solicitacao.getProtocolo(),
                solicitacao.getStatus(),
                solicitacao.getStatus().getDescricao(),
                solicitacao.getCep(),
                solicitacao.getEndereco(),
                solicitacao.getComplemento(),
                solicitacao.getPontoReferencia(),
                solicitacao.getDescricao(),
                solicitacao.getCreatedAt(),
                solicitacao.getUpdatedAt(),
                usuarioDTO,
                servicoDTO,
                anexos,
                historico
        );
    }

    // Converte anexo pra DTO
    private AnexoResponseDTO toAnexoResponse(Anexo anexo) {
        return new AnexoResponseDTO(
                anexo.getId(),
                anexo.getUrlArquivo(),
                anexo.getTipoMime(),
                anexo.getCreatedAt()
        );
    }

    // Converte histórico de status pra DTO
    private HistoricoStatusResponseDTO toHistoricoResponse(HistoricoStatus historico) {
        return new HistoricoStatusResponseDTO(
                historico.getId(),
                historico.getStatusAnterior(),
                historico.getStatusNovo(),
                historico.getObservacao(),
                historico.getChangedBy(),
                historico.getChangedAt()
        );
    }

    // Gerar protocolo único (ANO-XXXX)
    private String gerarProtocolo() {
        String ano = String.valueOf(Year.now().getValue());
        String codigo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return ano + "-" + codigo;
    }
}