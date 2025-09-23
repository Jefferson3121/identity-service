package com.identity_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = " \"user\"")
@Getter @Setter @NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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


    @NotNull(message = "No se igreso ningun tipo de usuario")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "users_types")
    private UsersTypes userType;
    private boolean stateLogin;

    public UserEntity(String name, int dni, String email, String password){
        this.name = name;
        this.dni = dni;
        this.email =email;
        this.password = password;
        this.userType = UsersTypes.EMPLOYEE;
        this.stateLogin = false;
    }





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
