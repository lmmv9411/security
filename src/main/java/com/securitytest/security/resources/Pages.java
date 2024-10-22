package com.securitytest.security.resources;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.securitytest.security.models.Usuario;
import com.securitytest.security.services.UsuarioService;

@Controller
@RequestMapping
public class Pages {

    public Pages(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        Usuario usuarioAuth = (Usuario) authentication.getPrincipal();

        Optional<Usuario> usuarioOptional = usuarioService
                .findByUserName(usuarioAuth.getUserName());

        Usuario usuario = usuarioOptional.get();

        model.addAttribute("username", usuario.getUserName());
        model.addAttribute("name", usuario.getName());
        model.addAttribute(
                "roles",
                usuario.getRoles()
                        .stream()
                        .map(rol -> rol.getNombre())
                        .toList());

        return "index";
    }

    private final UsuarioService usuarioService;
}
