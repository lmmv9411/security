package com.securitytest.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.securitytest.security.exceptions.customs.NotFoundException;
import com.securitytest.security.models.Rol;
import com.securitytest.security.models.Usuario;
import com.securitytest.security.repositories.RolRepository;
import com.securitytest.security.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.rolRepository = rolRepository;
    }

    public String encryptPassword(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    public boolean passwordMatches(String rawPassword, String encodePassword) {
        return this.passwordEncoder.matches(rawPassword, encodePassword);
    }

    public Optional<Usuario> findByUserName(String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario save(Usuario usuario) {

        usuario.setPassword(encryptPassword(usuario.getPassword()));

        final Set<Rol> roles = new HashSet<>();

        usuario.getRoles().forEach(rol -> {

            Optional<Rol> rolOptional = rolRepository.findById(rol.getId());

            rolOptional.ifPresentOrElse(roles::add, () -> {
                throw new NotFoundException("Rol no encontrado: " + rol.getId());
            });

        });

        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Usuario usuario, Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            throw new NotFoundException("Usuario no encontrado!");
        }

        Usuario usuarioDB = usuarioOptional.get();

        usuarioDB.setName(usuario.getName());
        usuarioDB.setEmail(usuario.getEmail());

        final Set<Rol> roles = new HashSet<>();

        usuario.getRoles().forEach(rol -> {

            Optional<Rol> rolOptional = rolRepository.findById(rol.getId());

            rolOptional.ifPresentOrElse(roles::add, () -> {
                throw new NotFoundException("Rol no encontrado: " + rol.getId());
            });

        });

        usuarioDB.setRoles(roles);

        return usuarioRepository.save(usuarioDB);
    }

}
