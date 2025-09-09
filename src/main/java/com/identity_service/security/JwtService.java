package com.identity_service.security;

import com.identity_service.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys; // Import nuevo
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secretBase64;

    @Value("${jwt.expiration-ms:3600000}") // por defecto 1h si no está configurado
    private long expirationMs;

    @Value("${jwt.issuer:mi-api}")
    private String issuer;


    // Convertimos el secreto Base64 en una SecretKey válida para HS256
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }



    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extraer el email (subject)
    public String extractUser(String token) {
        return getClaims(token).getSubject();
    }

    // Extraer el rol
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }



    // Validar token
    public boolean isTokenValid(String token, UserEntity user) {
        String username = extractUser(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

}