package com.carpinaon.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private String telefone;

    // Opcional - cidadão pode cadastrar depois
    // (Flutter: mostrar tooltip explicando que o CNS é o número da carteirinha do SUS,
    //  dá pra achar no app Meu SUS Digital ou na UBS do bairro)
    private String numeroCNS;

    // Opcional - RG do cidadão (mostra no perfil)
    private String rg;

    // Opcional - NIS do cidadão (programas sociais)
    private String nis;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
