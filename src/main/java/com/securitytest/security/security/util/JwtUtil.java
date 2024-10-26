package com.securitytest.security.security.util;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.securitytest.security.models.Usuario;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "jesus_es_mi_pastor_nada_me_faltara";
    private final long expiration_time = System.currentTimeMillis() + (1000 * 60 * 60); // 1 hora

    public String generateToken(Usuario usuario) {
        return JWT.create().withSubject(usuario.getUserName()).withClaim("id", usuario.getId()).withClaim("name", usuario.getName()).withClaim("roles", usuario.getRoles().stream().map(r -> "ROLE_" + r.getNombre()).toList()).withExpiresAt(new Date(expiration_time)) // 1 hora
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // Validar el token JWT
    public Optional<DecodedJWT> validateToken(String token) {

        if (token.isBlank()) return Optional.empty();

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return Optional.of(verifier.verify(token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}