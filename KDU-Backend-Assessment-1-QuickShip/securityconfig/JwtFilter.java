package com.example.QuickShip.securityconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws jakarta.servlet.ServletException, java.io.IOException {

        String header = request.getHeader("Authorization");

        try {
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtUtil.isValid(token)) {

                    String username = jwtUtil.getUsername(token);
                    List<String> roles = jwtUtil.getRoles(token);

                    var authorities = roles.stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                            .toList();

                    var auth = new UsernamePasswordAuthenticationToken(
                            username, null, authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // Invalid token
                    logger.warn("Invalid JWT token provided");
                    sendUnauthorizedError(response, "Invalid or expired token");
                    return;
                }
            } else {
                // Check if the endpoint requires authentication
                String requestPath = request.getRequestURI();
                if (!isPublicEndpoint(requestPath)) {
                    logger.warn("Missing authorization token for path: {}", requestPath);
                    sendUnauthorizedError(response, "Missing or invalid Authorization header");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("JWT Filter error: {}", e.getMessage());
            sendUnauthorizedError(response, "Authentication failed");
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/login") || 
               path.contains("/swagger-ui") || 
               path.contains("/v3/api-docs") ||
               path.equals("/h2");
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        String jsonResponse = new ObjectMapper().writeValueAsString(
            java.util.Map.of("error", message, "status", 401)
        );
        response.getWriter().write(jsonResponse);
    }
}
