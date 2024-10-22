package com.securitytest.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.securitytest.security.models.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

}
