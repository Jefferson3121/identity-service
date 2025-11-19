package com.identity_service.security;

import com.identity_service.dto.internal.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Clase responsable de la gestión de tokens JWT.
 *
 * Se encarga de:
 *  - Generar tokens firmados con una clave secreta (usando HS256).
 *  - Extraer información (claims) de un token, como el email.
 *  - Validar si un token es correcto y si no ha expirado.
 *
 * Los valores de expiración, emisor y clave se inyectan desde el archivo de configuración (application.properties).
 *
 * En resumen, centraliza toda la lógica relacionada con la creación y validación de JWT.
 */


@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secretBase64;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    @Value("${jwt.issuer:identity_service}")
    private String issuer;


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(TokenData tokenData) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(tokenData.email())
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }



    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}