package com.carpinaon.service;

import com.carpinaon.model.Anexo;
import com.carpinaon.model.Solicitacao;
import com.carpinaon.model.Usuario;
import com.carpinaon.model.enums.PerfilUsuario;
import com.carpinaon.repository.AnexoRepository;
import com.carpinaon.repository.SolicitacaoRepository;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

// Service de anexos - sobe arquivo pro Cloudflare R2 e controla o download
@Service
public class AnexoService {

    private static final long MAX_TAMANHO_BYTES = 5L * 1024 * 1024; // 5MB
    private static final List<String> TIPOS_PERMITIDOS =
            List.of("image/jpeg", "image/png", "application/pdf");

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${r2.bucket-name}")
    private String bucketName;

    // Sobe um arquivo pro bucket e registra o anexo na solicitação
    @Transactional
    public Anexo anexar(Long usuarioId, Long solicitacaoId, MultipartFile arquivo) {
        validarArquivo(arquivo);

        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        // Cidadão só anexa na solicitação dele; admin anexa em qualquer uma
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        boolean isAdmin = usuario.getRole() == PerfilUsuario.ADMIN;
        if (!isAdmin && !solicitacao.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para anexar nessa solicitação");
        }

        // Key única no bucket: solicitacoes/{id}/{uuid}.ext
        String extensao = extrairExtensao(arquivo.getOriginalFilename());
        String chave = "solicitacoes/" + solicitacaoId + "/" + UUID.randomUUID() + extensao;

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(chave)
                            .contentType(arquivo.getContentType())
                            .build(),
                    RequestBody.fromBytes(arquivo.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível ler o arquivo enviado. Tente novamente.");
        } catch (S3Exception e) {
            throw new RuntimeException("Não foi possível enviar o arquivo. Verifique se o armazenamento está configurado.");
        }

        Anexo anexo = new Anexo(solicitacao, chave, arquivo.getOriginalFilename(),
                arquivo.getContentType(), arquivo.getSize());
        return anexoRepository.save(anexo);
    }

    // Baixa o arquivo do bucket (bytes + metadados)
    public DadosDownload baixar(Long usuarioId, Long solicitacaoId, Long anexoId) {
        Anexo anexo = anexoRepository.findById(anexoId)
                .orElseThrow(() -> new RuntimeException("Anexo não encontrado"));

        if (!anexo.getSolicitacao().getId().equals(solicitacaoId)) {
            throw new RuntimeException("Anexo não pertence a essa solicitação");
        }

        // Só o dono da solicitação ou admin pode baixar
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        boolean isAdmin = usuario.getRole() == PerfilUsuario.ADMIN;
        if (!isAdmin && !anexo.getSolicitacao().getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para baixar esse anexo");
        }

        try {
            byte[] bytes = s3Client.getObject(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(anexo.getUrlArquivo())
                            .build())
                    .readAllBytes();
            return new DadosDownload(bytes, anexo.getTipoMime(), anexo.getNomeArquivo());
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Arquivo não encontrado no armazenamento");
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível baixar o arquivo");
        } catch (S3Exception e) {
            throw new RuntimeException("Não foi possível baixar o arquivo");
        }
    }

    // Valida tamanho e formato antes de gastar request
    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new RuntimeException("Nenhum arquivo enviado");
        }

        if (arquivo.getSize() > MAX_TAMANHO_BYTES) {
            throw new RuntimeException("O arquivo não pode ultrapassar 5MB. Envie uma imagem ou PDF menor.");
        }

        String contentType = arquivo.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Formato não permitido. Envie apenas JPG, PNG ou PDF.");
        }
    }

    // Pega a extensão do arquivo original (ex: .jpg) pra montar a key no bucket
    private String extrairExtensao(String nomeArquivo) {
        String nome = StringUtils.hasText(nomeArquivo) ? nomeArquivo : "arquivo";
        int idx = nome.lastIndexOf('.');
        return idx >= 0 ? nome.substring(idx).toLowerCase() : "";
    }

    // Retorno do download (bytes + metadados do arquivo)
    public record DadosDownload(byte[] bytes, String contentType, String nomeArquivo) {
    }
}