package com.identity_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Entity
@Table(name = " \"user\"")
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
    private boolean stateLogin;
    private boolean enabled;

    public UserEntity(String name, int dni, String email, String password){
        this.name = name;
        this.dni = dni;
        this.email =email;
        this.password = password;
        this.userType = UsersTypes.EMPLOYEE;
        this.stateLogin = false;
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
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
