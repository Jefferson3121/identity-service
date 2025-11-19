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


/**
 * Entidad que representa a un usuario dentro del sistema de autenticación.
 *
 * Esta clase implementa la interfaz UserDetails de Spring Security, lo que permite
 * que sus instancias sean reconocidas por el framework para gestionar la autenticación
 * y autorización de usuarios.
 *
 * Contiene información básica del usuario como nombre, DNI, email y contraseña,
 * además del tipo de usuario (userType), el estado de inicio de sesión (stateLogin)
 * y si la cuenta está habilitada (enabled).
 *
 * El campo userType se utiliza para asignar roles mediante la interfaz GrantedAuthority,
 * lo que determina los permisos del usuario dentro del sistema.
 */



@Entity
@Table(name = "\"user\"")
@Getter @Setter @NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @NotBlank(message = "No se ingreso el nombre")
    private String name;

    @NotNull(message = "No se ingreso el DNI")
    private Integer dni;

    @NotBlank(message = "No se ingreso el email")
    private String email;

    @NotBlank(message = "NO se ingreso ninguna contraseña")
    private String password;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "users_types")
    private UsersTypes userType;

    private boolean enabled;

    public UserEntity(String name, int dni, String email, String password){
        this.name = name;
        this.dni = dni;
        this.email =email;
        this.password = password;
        this.userType = UsersTypes.EMPLOYEE;
        this.enabled = false;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + this.userType);

        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }//La cuenta nunca expira

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }//Las credenciales nunca expiran

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
