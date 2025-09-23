package com.identity_service.service;

import com.identity_service.infrastructure.mapper.UserMapper;
import com.identity_service.model.UserEntity;
import com.identity_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


//    private UserRepository userRepository = Mockito.mock(UserRepository.class);
//    private UserMapper userMapper = Mockito.mock(UserMapper.class);
//    private UserService userService = new UserService(userMapper, userRepository);

    @Test
   public void addUser_deberiaRetornarFalseSiEmailYaExiste(){
        UserEntity user = new UserEntity();
        user.setEmail("jeff@example.com");

        // Simulamos que el email ya existe
        Mockito.when(userRepository.existsByEmail("jeff@example.com")).thenReturn(true);

        boolean resultado = userService.addUser(user);

        System.out.println("Resultado: " + resultado);

        assertFalse(resultado);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        System.out.println("Resultado: " + resultado);
    }

    @Test
    public  void addUser_deberiaGuardarUsuarioSiEmailNoExiste() {
        UserEntity user = new UserEntity();
        user.setEmail("nuevo@example.com");

        // Simulamos que el email NO existe
        Mockito.when(userRepository.existsByEmail("nuevo@example.com")).thenReturn(false);

        boolean resultado = userService.addUser(user);

        assertTrue(resultado);
        Mockito.verify(userRepository).save(user);
        System.out.println("Resultado " + resultado);

    }



    @Test
    void changePassword_deberiaCambiarPasswordSiEsCorrecta() {
        String email = "jeff@example.com";
        String currentPassword = "1234";
        String newPassword = "abcd";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword("1234");


        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        boolean resultado = userService.changePassword(email, currentPassword, newPassword);

        assertTrue(resultado);
        System.out.println("Si cambio la contraseña");

        assertEquals(newPassword, user.getPassword());
        System.out.println("Contraseña modificada a " + user.getPassword() + "Contraseña modificada esperada " + newPassword);
    }

    @Test
    void changePassword_deberiaFallarSiPasswordActualEsIncorrecta() {
        String email = "jeff@example.com";
        String currentPassword = "wrong";
        String newPassword = "abcd";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword("1234");

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        boolean resultado = userService.changePassword(email, currentPassword, newPassword);

        assertFalse(resultado);
        assertEquals("1234", user.getPassword());
    }

    @Test
    void changePassword_deberiaFallarSiUsuarioNoExiste() {
        String email = "noexiste@example.com";

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        boolean resultado = userService.changePassword(email, "1234", "abcd");

        assertFalse(resultado);
    }

}
