package com.epam.service;

import com.epam.service.AuthService;
import com.epam.service.JWTService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
 class AuthServiceTest {

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
     void testGenerateToken() {
        String username = "testuser";
        String expectedToken = "generatedToken123";
        Mockito.when(jwtService.generateToken(username)).thenReturn(expectedToken);
        String generatedToken = authService.generateToken(username);
        assertEquals(expectedToken, generatedToken);
        Mockito.verify(jwtService, times(1)).generateToken(username);
    }

    @Test
     void testValidateToken() {
        String token = "validToken123";
        authService.validateToken(token);
        verify(jwtService, times(1)).validateToken(token);
    }
}
