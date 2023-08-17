package com.example.demo.servicetest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.security.Key;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.service.JWTService;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
 class JWTServiceTest {
    @InjectMocks
    private JWTService jwtService;

    @Mock
    private JwtParserBuilder jwtParserBuilder;

    @Mock
    private JwtParser jwtParser;

    @Mock
    private Keys keys;

//    @BeforeEach
//    public void setup() {
//        jwtService = new JWTService();
//        jwtService.SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//    }

//    @Test
//    public void testValidateToken_ValidToken() {
//        // Arrange
//        JwtParserBuilder parserBuilder;
//        when(jwtParserBuilder.setSigningKey(any(Key.class))).thenReturn(parserBuilder);
//        when(parserBuilder.build()).thenReturn(jws);
//
//        // Act and Assert
//        assertDoesNotThrow(() -> jwtService.validateToken("validToken123"));
//    }

    @Test
    void testGenerateToken() {
        String userName = "testUser";
        JWTService jwtService = spy(JWTService.class);
        String generatedToken = jwtService.generateToken(userName);
        assertNotNull(generatedToken);
    }



}