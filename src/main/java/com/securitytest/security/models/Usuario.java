package com.securitytest.security.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email", "userName" }) })
@JsonIgnoreProperties(value = "password", allowGetters = false, allowSetters = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "userName cant not be empty")
    @Column(nullable = false, length = 50, unique = true)
    private String userName;

    @NotEmpty(message = "name cant not be empty")
    @Column(nullable = false, length = 30)
    private String name;

    @NotEmpty(message = "email cant not be empty")
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @NotEmpty(message = "password cant not be empty")
    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    @JsonIgnoreProperties("usuarios")
    private Set<Rol> roles = new HashSet<>();

}
