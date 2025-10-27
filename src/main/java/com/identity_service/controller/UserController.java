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
    @DeleteMapping("/delete/{dni}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer dni){
        userService.deleteUser(dni);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado");
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequestDTO changeEmailRequestDTO){
        userService.changeEmail(changeEmailRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Email modificada correctamente");
    }

    @PreAuthorize(("hasAnyRole('ADMIN', 'EMPLOYEE')"))
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO change){

        userService.changePassword(change);
            return ResponseEntity.status(HttpStatus.OK).body("La contraseña ha sido modifcada correctamente");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/enable-user")
    public ResponseEntity<String> enableUser(@RequestParam String email){

        userService.enableUser(email);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario habilitado");
    }
}
