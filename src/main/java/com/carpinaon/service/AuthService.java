package com.carpinaon.service;

import com.carpinaon.config.JwtUtil;
import com.carpinaon.dto.auth.AuthRequestDTO;
import com.carpinaon.dto.auth.AuthResponseDTO;
import com.carpinaon.dto.usuario.UsuarioRequestDTO;
import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import com.carpinaon.model.Usuario;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// Service de autenticação - login e cadastro
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Login do cidadão
    public AuthResponseDTO login(AuthRequestDTO request) {
        // Busca o usuário pelo CPF
        Usuario usuario = usuarioRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("CPF ou senha inválidos"));

        // Verifica se a senha bate
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("CPF ou senha inválidos");
        }

        // Gera o token JWT
        String token = jwtUtil.gerarToken(usuario.getCpf());

        // Monta a resposta
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUsuario(toResponse(usuario));
        response.setExpiracao(LocalDateTime.now().plusHours(24));

        return response;
    }

    // Cadastro de novo cidadão
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        // Verifica se o CPF já existe
        if (usuarioRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        // Verifica se o email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Cria o usuário com senha criptografada
        Usuario usuario = new Usuario(
                request.getNome(),
                request.getCpf(),
                request.getEmail(),
                request.getTelefone(),
                passwordEncoder.encode(request.getSenha())
        );

        // CNS é opcional - cidadão pode cadastrar depois
        if (request.getNumeroCNS() != null) {
            usuario.setNumeroCNS(request.getNumeroCNS());
        }

        usuario = usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    // Converte entidade pra DTO de resposta
    private UsuarioResponseDTO toResponse(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setCpf(usuario.getCpf());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setNumeroCNS(usuario.getNumeroCNS());
        dto.setStatusVerificacao(usuario.getStatusVerificacao());
        dto.setCreatedAt(usuario.getCreatedAt());
        return dto;
    }
}
