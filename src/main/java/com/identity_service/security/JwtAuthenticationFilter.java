package com.identity_service.security;

import java.io.IOException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filtro personalizado de autenticación JWT.
 *
 * Esta clase intercepta todas las solicitudes HTTP entrantes y se ejecuta una sola vez por petición
 * (gracias a la herencia de OncePerRequestFilter).
 *
 * Su función principal es validar si la solicitud contiene un token JWT válido en el encabezado
 * "Authorization". Si el token es correcto:
 *   - Se extrae el email del usuario desde el token.
 *   - Se cargan los detalles del usuario mediante el UserDetailsService.
 *   - Se crea un objeto de autenticación y se establece en el SecurityContextHolder,
 *     lo que permite que el resto de la aplicación reconozca al usuario como autenticado.
 *
 * Si el token es inválido, expirado o ausente, simplemente deja que el flujo continúe sin
 * establecer autenticación (lo que normalmente resultará en un 401 cuando se intente acceder
 * a rutas protegidas).
 */



@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();


        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);


        try {
            String email = tokenManager.extractEmail(token);
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido o expirado
            // No se autentica a nadie, simplemente continúa sin contexto
        }
        String email = tokenManager.extractEmail(token);


        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (tokenManager.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
