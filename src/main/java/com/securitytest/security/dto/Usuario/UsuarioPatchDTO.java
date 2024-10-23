package com.securitytest.security.dto.Usuario;

import com.securitytest.security.models.Rol;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioPatchDTO {

    private String email;
    private String name;
    private Set<Rol> roles;
    private String userName;

}
