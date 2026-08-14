package com.carpinaon.service;

import com.carpinaon.config.JwtUtil;
import com.carpinaon.model.Usuario;
import com.carpinaon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Helper pra pegar o usuário logado a partir do token JWT do header
@Service
public class UsuarioLogadoService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Extrai o CPF do token e busca o usuário completo
    public Usuario buscar(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de autenticação não informado");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtUtil.tokenValido(token)) {
            throw new RuntimeException("Token inválido ou expirado");
        }

        String cpf = jwtUtil.extrairCpf(token);
        return usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}