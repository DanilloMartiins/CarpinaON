package com.carpinaon.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.carpinaon.model.enums.PerfilUsuario;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String numeroCNS;
    private String rg;
    private String nis;
    private Boolean statusVerificacao;
    private PerfilUsuario role;
    private LocalDateTime createdAt;
}
