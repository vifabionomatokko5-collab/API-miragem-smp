package br.com.miragem.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    private final String apiToken;

    public ApiTokenFilter(
            @Value("${miragem.api.token:}") String apiToken
    ) {
        this.apiToken = apiToken;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Health público
        if (path.equals("/api/v1/health")
                || path.equals("/actuator/health")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Se o token não foi configurado, bloqueia a API
        if (apiToken == null || apiToken.isBlank()) {
            unauthorized(
                    response,
                    "Token da API não configurado."
            );
            return;
        }

        String authorization =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            unauthorized(
                    response,
                    "Token de autenticação ausente."
            );
            return;
        }

        String receivedToken =
                authorization.substring(7).trim();

        if (receivedToken.isBlank()) {
            unauthorized(
                    response,
                    "Token de autenticação vazio."
            );
            return;
        }

        if (!secureEquals(apiToken, receivedToken)) {
            unauthorized(
                    response,
                    "Token de autenticação inválido."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean secureEquals(
            String expected,
            String received
    ) {
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                received.getBytes(StandardCharsets.UTF_8)
        );
    }

    private void unauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(
                """
                {
                  "success": false,
                  "status": 401,
                  "message": "%s"
                }
                """.formatted(message)
        );
    }
}
