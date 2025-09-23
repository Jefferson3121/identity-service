package com.identity_service.integration;

import com.identity_service.IdentityService;
import com.identity_service.model.UserEntity;
import com.identity_service.model.UsersTypes;
import com.identity_service.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.Optional;
@SpringBootTest(classes = IdentityService.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void limpiarBD() {
        userRepository.deleteAll();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Order(1)
    void postUsuario_deberiaGuardarEnBD() throws Exception {
        String jsonUsuario = """
            {
                "name": "Jefferson",
                "dni": 8274639,
                "email": "jeff@example.com",
                "password": "12345849383",
                "userType": "ADMIN"
            }
        """;

        mockMvc.perform(post("/api/login_service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUsuario))
                .andExpect(status().isCreated());

        Optional<UserEntity> usuario = userRepository.findUserByEmail("jeff@example.com");
        assertTrue(usuario.isPresent());
        assertEquals("12345849383", usuario.get().getPassword());
    }


    @Test
    @Order(2)
    void login_deberiaRetornarTokenSiCredencialesSonValidas() throws Exception {

        UserEntity user = new UserEntity();
        user.setEmail("jeff@example.com");
        user.setPassword("12345678919");
        user.setName("Jefferson");
        user.setDni(8274639);
        user.setUserType(UsersTypes.ADMIN);
        userRepository.save(user);

        String jsonLogin = """
            {
                "email": "jeff@example.com",
                "password": "12345678919"
            }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
