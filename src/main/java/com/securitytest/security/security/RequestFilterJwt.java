package com.securitytest.security.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.securitytest.security.models.Usuario;
import com.securitytest.security.security.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilterJwt extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private final List<String> listUrls = List.of(
            "/login", "/api/auth/", "/favicon.ico", "/js/", "/css/");

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return listUrls.stream().anyMatch(url -> request.getRequestURI().startsWith(url));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        var cookieSesion = WebUtils.getCookie(request, "sesion");

        if (cookieSesion == null || cookieSesion.getValue().isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        var decodedJwt = jwtUtil.validateToken(cookieSesion.getValue());

        if (decodedJwt.isEmpty() || decodedJwt.get().getExpiresAt().before(new Date())) {
            removeCookie(response);
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(decodedJwt.get());

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(DecodedJWT decodedJwt) {
        String userName = decodedJwt.getSubject();
        String name = decodedJwt.getClaim("name").asString();
        List<String> roles = decodedJwt.getClaim("roles").asList(String.class);

        Usuario usuario = new Usuario();
        usuario.setName(name);
        usuario.setUserName(userName);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new).toList();

        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookieSesion = new Cookie("sesion", null);
        cookieSesion.setMaxAge(0);
        cookieSesion.setPath("/");
        response.addCookie(cookieSesion);
    }

}
