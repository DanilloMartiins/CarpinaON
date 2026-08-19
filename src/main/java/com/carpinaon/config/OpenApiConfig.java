package com.carpinaon.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Config do Swagger UI - título, descrição e o esquema de autenticação Bearer
// Sem o security scheme o botão Authorize não envia o header Authorization direito
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carpinaonOpenAPI() {
        String schemeName = "bearerAuth";

        return new OpenAPI()
                // Todas as rotas podem usar o token por padrão
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components().addSecuritySchemes(schemeName,
                        new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("CarpinaON API")
                        .description("Portal de serviços municipais de Carpina. Rotas públicas (catálogo, solicitações, eventos) e administrativas (/api/v1/admin, só ADMIN).")
                        .version("0.1.0"));
    }
}