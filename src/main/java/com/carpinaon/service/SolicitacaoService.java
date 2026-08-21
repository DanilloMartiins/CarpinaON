package com.carpinaon.service;

import com.carpinaon.dto.categoria.CategoriaResumidoDTO;
import com.carpinaon.dto.servico.ServicoResumidoDTO;
import com.carpinaon.dto.servico.ServicoResponseDTO;
import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import com.carpinaon.dto.solicitacao.AnexoResponseDTO;
import com.carpinaon.dto.solicitacao.HistoricoStatusResponseDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoAdminResumoDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoRequestDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResumidoDTO;
import com.carpinaon.dto.solicitacao.SolicitacaoResponseDTO;
import com.carpinaon.model.Anexo;
import com.carpinaon.model.HistoricoStatus;
import com.carpinaon.model.Notificacao;
import com.carpinaon.model.Servico;
import com.carpinaon.model.Solicitacao;
import com.carpinaon.model.Usuario;
import com.carpinaon.model.enums.StatusSolicitacao;
import com.carpinaon.repository.AnexoRepository;
import com.carpinaon.repository.HistoricoStatusRepository;
import com.carpinaon.repository.NotificacaoRepository;
import com.carpinaon.repository.ServicoRepository;
import com.carpinaon.repository.SolicitacaoRepository;
import com.carpinaon.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
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

    @Autowired
    private NotificacaoRepository notificacaoRepository;

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
                usuario
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

    // Listar todas as solicitações (admin) com paginação e filtros
    public Page<SolicitacaoAdminResumoDTO> listarAdmin(StatusSolicitacao status, LocalDate dataInicio,
                                                       LocalDate dataFim, String termo, Pageable pageable) {
        Specification<Solicitacao> spec = montarFiltro(status, dataInicio, dataFim, termo);
        return solicitacaoRepository.findAll(spec, pageable).map(this::toAdminResumo);
    }

    // Buscar solicitação por id (admin)
    public SolicitacaoResponseDTO buscarPorId(Long id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
        return toResponse(solicitacao);
    }

    // Monta o filtro dinâmico da listagem admin
    private Specification<Solicitacao> montarFiltro(StatusSolicitacao status, LocalDate dataInicio,
                                                    LocalDate dataFim, String termo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dataInicio.atStartOfDay()));
            }

            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), dataFim.atTime(LocalTime.MAX)));
            }

            if (termo != null && !termo.isBlank()) {
                String termoLike = "%" + termo.trim().toUpperCase() + "%";
                predicates.add(cb.like(cb.upper(root.get("protocolo")), termoLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Converte entidade pra DTO resumido da listagem admin
    private SolicitacaoAdminResumoDTO toAdminResumo(Solicitacao solicitacao) {
        return new SolicitacaoAdminResumoDTO(
                solicitacao.getId(),
                solicitacao.getProtocolo(),
                solicitacao.getStatus(),
                solicitacao.getStatus().getDescricao(),
                solicitacao.getUsuario().getNome(),
                solicitacao.getServico().getNome(),
                solicitacao.getEndereco(),
                solicitacao.getCreatedAt()
        );
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

        // Busca quem mudou o status (pro histórico guardar o usuário completo)
        Usuario usuarioLogado = usuarioRepository.findById(changedBy)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        historicoStatusRepository.save(new HistoricoStatus(
                solicitacao,
                statusAnterior,
                novoStatus,
                observacao,
                usuarioLogado
        ));

        // Manda notificação pro cidadão sobre a mudança de status
        notificacaoRepository.save(new Notificacao(
                solicitacao.getUsuario(),
                solicitacao,
                "Atualização do protocolo " + solicitacao.getProtocolo(),
                "Sua solicitação " + solicitacao.getProtocolo() + " mudou de "
                        + statusAnterior.getDescricao() + " para " + novoStatus.getDescricao() + "."
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
                .map(this::converterAnexo)
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
                categoria,
                servico.getCreatedAt(),
                servico.getUpdatedAt()
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

    // Converte anexo pra DTO (com url do download autenticado)
    public AnexoResponseDTO converterAnexo(Anexo anexo) {
        String urlDownload = "/api/v1/solicitacoes/" + anexo.getSolicitacao().getId()
                + "/anexos/" + anexo.getId() + "/arquivo";
        return new AnexoResponseDTO(
                anexo.getId(),
                anexo.getUrlArquivo(),
                anexo.getNomeArquivo(),
                anexo.getTipoMime(),
                anexo.getTamanho(),
                urlDownload,
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