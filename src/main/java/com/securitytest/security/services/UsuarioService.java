package com.securitytest.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.securitytest.security.dto.Usuario.UsuarioPatchDTO;
import com.securitytest.security.exceptions.customs.BadRequestException;
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
    public Usuario patch(UsuarioPatchDTO usuarioPatch, Long id) {

        var usuarioDB = finById(id);

        var count = new AtomicInteger();

        Optional.ofNullable(usuarioPatch.getEmail())
                .filter(email -> !email.isBlank())
                .ifPresentOrElse(usuarioDB::setEmail, count::incrementAndGet);

        Optional.ofNullable(usuarioPatch.getName())
                .filter(name -> !name.isBlank())
                .ifPresentOrElse(usuarioDB::setName, count::incrementAndGet);

        Optional.ofNullable(usuarioPatch.getUserName())
                .filter(userName -> !userName.isBlank())
                .ifPresentOrElse(usuarioDB::setUserName, count::incrementAndGet);

        Optional.ofNullable(usuarioPatch.getRoles())
                .filter(roles -> !roles.isEmpty())
                .ifPresentOrElse((roles) -> {
                    roles.forEach(rol -> {
                        if (rolRepository.findById(rol.getId()).isEmpty()) {
                            throw new NotFoundException("Rol no encontrado: " + rol.getId());
                        }
                    });
                    usuarioDB.setRoles(roles);
                }, count::incrementAndGet);

        if (count.get() == usuarioPatch.getClass().getDeclaredFields().length) {
            throw new BadRequestException("No hay campos valido a actualizar!");
        }

        return usuarioRepository.save(usuarioDB);

    }

    public Usuario finById(Long id) {

        var optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isEmpty()) {
            throw new NotFoundException("Usuario not found");
        }

        return optionalUsuario.get();
    }

}
