package com.carpinaon.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Guarda de trânsito: limita a quantidade de requisições
// Cada prefeitura (slug no path) tem o próprio balde de acesso
// Assim, uma pref em mutirão não engole o recurso das outras
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Rotas que não têm prefeitura no path (ex: /api/v1/auth/...)
    private static final Set<String> ROTAS_SEM_TENANT = Set.of("auth");

    // Rotas que o rate limit não bloqueia (Swagger carrega vários arquivos de uma vez)
    private static final Set<String> ROTAS_LIBERADAS = Set.of(
            "/swagger-ui", "/swagger-ui.html", "/v3/api-docs"
    );

    // Limite por prefeitura: 50 requisições a cada 10 segundos
    private static final int LIMITE_TENANT = 50;
    private static final Duration JANELA_TENANT = Duration.ofSeconds(10);

    // Fallback pra rotas sem tenant: 20 requisições a cada 10 segundos por IP
    private static final int LIMITE_IP = 20;
    private static final Duration JANELA_IP = Duration.ofSeconds(10);

    // Um balde por prefeitura ou por IP (cresce conforme chegam acessos)
    private final ConcurrentMap<String, Bucket> baldes = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Swagger e documentação passam direto (não é API)
        String uri = request.getRequestURI();
        for (String rota : ROTAS_LIBERADAS) {
            if (uri.startsWith(rota)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String chave = montaChave(request);

        Bucket balde = baldes.computeIfAbsent(chave, k -> criaBalde(k));

        if (balde.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
            "{\"mensagem\": \"Estamos em congestionamento: muitos acessos no momento. Tente novamente em instantes.\"}"
        );
    }

    // Se a URL tem prefeitura, a chave é o slug dela (todas as pessoas da pref somam no mesmo balde)
    // Se não tem (auth), a chave é o IP
    private String montaChave(HttpServletRequest request) {
        String tenant = extraiTenant(request.getRequestURI());
        if (tenant != null) {
            return "tenant:" + tenant;
        }
        return "ip:" + request.getRemoteAddr();
    }

    // Pega o slug no path: /api/v1/carpina/... -> "carpina"
    // Ignora as rotas globais (auth) que não são prefeitura
    private String extraiTenant(String uri) {
        String[] partes = uri.split("/");
        for (int i = 0; i < partes.length; i++) {
            if ("v1".equals(partes[i]) && i + 1 < partes.length) {
                String candidato = partes[i + 1];
                if (!candidato.isEmpty() && !ROTAS_SEM_TENANT.contains(candidato)) {
                    return candidato;
                }
            }
        }
        return null;
    }

    private Bucket criaBalde(String chave) {
        boolean ehTenant = chave.startsWith("tenant:");
        int limite = ehTenant ? LIMITE_TENANT : LIMITE_IP;
        Duration janela = ehTenant ? JANELA_TENANT : JANELA_IP;

        Bandwidth banda = Bandwidth.classic(limite, Refill.greedy(limite, janela));
        return Bucket4j.builder().addLimit(banda).build();
    }
}
