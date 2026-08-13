package com.carpinaon.controller;

import com.carpinaon.dto.auth.AuthRequestDTO;
import com.carpinaon.dto.auth.AuthResponseDTO;
import com.carpinaon.dto.usuario.UsuarioRequestDTO;
import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import com.carpinaon.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller de autenticação - login e cadastro
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/auth/cadastrar
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = authService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
