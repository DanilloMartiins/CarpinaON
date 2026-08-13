package com.carpinaon.dto.auth;

import com.carpinaon.dto.usuario.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private UsuarioResponseDTO usuario;
    private LocalDateTime expiracao;
}
