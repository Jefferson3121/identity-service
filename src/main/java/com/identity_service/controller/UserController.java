package com.identity_service.controller;

import com.identity_service.dtos.ChangeEmailRequest;
import com.identity_service.dtos.ChangePasswordRequestDTO;
import com.identity_service.dtos.UserRequestDTO;
import com.identity_service.dtos.UserResponseDTO;
import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.service.UserService;
import jakarta.transaction.Transactional;
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
    @Transactional
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        if (userRequestDTO.dni() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO hay dni");
        }
        boolean created = userService.addUser(userMapper.toUserEntity(userRequestDTO));

        if (!created) {
            return ResponseEntity.
                    status(HttpStatus.CONFLICT)
                    .body("El usario ya existe");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usario creado correctamente");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@Valid @RequestParam String email){

       return userService.getUserByEmail(email)
                .map(userResponseDTO -> ResponseEntity.ok(userResponseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("/all")
    //@Valid @NotBlank @RequestParam String luzVerde
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();

        if (users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(users);

    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @NotNull @RequestParam Integer dni){

        String token = "admin";

        if ( !token.equalsIgnoreCase("admin")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Los empleados no puden eliminar un usuario ( no tiene autorizacion) ");
           // return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            //lanzar excepcion si es buena ide ovbio
        }

        if (userService.deleteUser(dni)){
            return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado");
        }

        //Por ahora solo un error del servidor desconocido
        return ResponseEntity.internalServerError().body("Erro desconocido__");
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequest change){

        if (userService.changeEmail(change.currentEmail(), change.newEmail(),change.password())){
            return ResponseEntity.status(HttpStatus.OK).body("Email modificada correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("NO se pudo cambiar el email");
    }


    @PreAuthorize(("hasAnyRole('ADMIN', 'EMPLOYEE')"))
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO change){

        if (userService.changePassword(change.email(), change.currentPasswor(), change.newPassword())){
            return ResponseEntity.status(HttpStatus.OK).body("La contraseña ha sido modifcada correctamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("La contraseña no se modifico");
    }
}


