package com.identity_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;



@Entity
@Getter
@Setter
@Table(name = " \"user\"")
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private int id;

    @NotBlank(message = "El nombre llega nullo vacio a userEntity")
    private String name;


    @NotNull(message = "DNI llega vacio a userEntity")
    private Integer dni;

    @NotBlank(message = "EMail llega vacio a userEntity")
    private String email;

    @NotBlank(message = "Contraseña no puede estar vacian en userEntity")
    private String password;


    @NotNull(message = "el tipo de usuario esta llegando vacio a userEntity")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "users_types")
    private UsersTypes userType;
    private boolean stateLogin;

    public UserEntity(String name, int dni, String email, String password){
        this.name = name;
        this.dni = dni;
        this.email =email;
        this.password = password;
        this.userType = null;
        this.stateLogin = false;
    }


    //para decidir qué puede hacer un usuario una vez autenticado.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + this.userType);

        return Collections.singletonList(authority);

    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    //Indica si la cuenta del usuario sigue vigente o ya expiró.
    @Override
    public boolean isAccountNonExpired() {
        return true; // o lógica si manejas expiración
    }


    //Verifica si la cuenta está bloqueada (por ejemplo, por demasiados intentos fallidos de login).
    @Override
    public boolean isAccountNonLocked() {
        return true; // o usar campo bloqueado
    }

    //Revisa si las credenciales (contraseña) siguen siendo válidas.
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // o usar campo de expiración
    }

    //Le dice a Spring Security si el usuario está habilitado para autenticarse.
    @Override
    public boolean isEnabled() {
        return stateLogin;
    }
}
