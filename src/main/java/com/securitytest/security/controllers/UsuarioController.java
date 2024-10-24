package com.securitytest.security.controllers;

import com.securitytest.security.dto.Usuario.UsuarioPatchDTO;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securitytest.security.models.Usuario;
import com.securitytest.security.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("{id}")
    public Usuario finById(@PathVariable Long id) {
        return usuarioService.finById(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@Valid @RequestBody Usuario usuario) {
        return new ResponseEntity<>(
                usuarioService.save(usuario),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public Usuario patch(
            @PathVariable Long id,
            @RequestBody UsuarioPatchDTO usuario) {

        return usuarioService.patch(usuario, id);
    }

}
