package com.securitytest.security.controllers;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securitytest.security.dto.UsuarioLoginDTO;
import com.securitytest.security.exceptions.customs.BadRequestException;
import com.securitytest.security.exceptions.customs.NotFoundException;
import com.securitytest.security.models.Usuario;
import com.securitytest.security.security.util.JwtUtil;
import com.securitytest.security.services.UsuarioService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public void login(
            @Valid @RequestBody UsuarioLoginDTO usuario,
            HttpServletResponse response) throws IOException {

        Optional<Usuario> usuarioOptional = usuarioService.findByUserName(usuario.getUserName());

        if (usuarioOptional.isEmpty()) {
            throw new NotFoundException("User not found: " + usuario.getUserName());
        }

        var usuarioDB = usuarioOptional.get();

        if (!usuarioService.passwordMatches(
                usuario.getPassword(),
                usuarioDB.getPassword())) {
            throw new BadRequestException("Passwords dont match!");
        }

        final String token = jwtUtil.generateToken(usuarioDB);

        Cookie cookieSesion = new Cookie("sesion", token);

        cookieSesion.setHttpOnly(true);
        cookieSesion.setMaxAge((int) Duration.ofHours(1).toMillis());
        cookieSesion.setPath("/");

        response.addCookie(cookieSesion);

        response.sendRedirect("/");

    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {

        Cookie cookieSesion = new Cookie("sesion", null);
                
        cookieSesion.setMaxAge(0);
        cookieSesion.setPath("/");

        response.addCookie(cookieSesion);

        response.sendRedirect("/login");

    }

}
