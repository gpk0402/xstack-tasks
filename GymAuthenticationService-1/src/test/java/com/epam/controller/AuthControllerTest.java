package com.epam.controller;

import com.epam.AuthRequest;
import com.epam.exception.InvalidAccessException;
import com.epam.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService service;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void testGetTokenValidUser() throws Exception {
        AuthRequest authRequest = new AuthRequest("username", "password");
        String requestBody = objectMapper.writeValueAsString(authRequest);
        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer((invocation) -> {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new TestingAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(), authorities);
        });
        Mockito.when(service.generateToken(any(String.class))).thenReturn("Lavanya");
        mockMvc.perform(post("/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    void testGetToken_InvalidCredentials() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InvalidAccessException("Invalid credentials"));

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testValidateTokenWithValidToken() throws Exception {
        String token = "valid_token";
        Mockito.doNothing().when(service).validateToken(any(String.class));
        mockMvc.perform(get("/auth/validate")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));
    }
}
