package com.identity_service.controller;

import com.identity_service.dto.ChangeEmailRequestDTO;
import com.identity_service.dto.ChangePasswordRequestDTO;
import com.identity_service.dto.UserResponseDTO;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login_service")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public  UserController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@Valid @RequestParam String email){

       return userService.getUserByEmail(email)
                .map(userResponseDTO -> ResponseEntity.ok(userResponseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();

        if (users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(users);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid  @NotNull @RequestParam Integer dni){

        if (userService.deleteUser(dni)){
            return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado");
        }
        return ResponseEntity.internalServerError().body("Error desconocido__");
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequestDTO change){

        if (userService.changeEmail(change.currentEmail(), change.newEmail(),change.password())){
            return ResponseEntity.status(HttpStatus.OK).body("Email modificada correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No se pudo cambiar el email");
    }


    @PreAuthorize(("hasAnyRole('ADMIN', 'EMPLOYEE')"))
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO change){

        if (userService.changePassword(change.email(), change.currentPasswor(), change.newPassword())){
            return ResponseEntity.status(HttpStatus.OK).body("La contraseña ha sido modifcada correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("La contraseña no se modifico");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/enable-user")
    public ResponseEntity<String> enableUser(@RequestParam String email){

        userService.enableUser(email);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario habilitado");
    }
}
