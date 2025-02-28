package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@WebMvcTest(RegistrationLoginController.class)
//@AutoConfigureMockMvc(addFilters = false)
/*public class RegistrationLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setMail("test@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.findByMail(user.getMail())).thenReturn(null);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserMailAlreadyExists() throws Exception {
        User user = new User();
        user.setMail("test@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.findByMail(user.getMail())).thenReturn(user);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("mail already exists"));
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
        User user = new User();
        user.setMail("test@example.com");
        user.setPassword("password");

        // Simuler un objet Authentication pour le succès de l'authentification
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword());

        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword())))
                .thenReturn(authentication);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testLoginUserInvalidCredentials() throws Exception {
        User user = new User();
        user.setMail("test@example.com");
        user.setPassword("wrongPassword");

        Mockito.doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword()));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid mail or password"));
    }
}*/
