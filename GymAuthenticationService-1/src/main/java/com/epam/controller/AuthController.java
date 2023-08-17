package com.epam.controller;

import com.epam.AuthRequest;
import com.epam.exception.InvalidAccessException;
import com.epam.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private  AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new InvalidAccessException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validate(@RequestParam("token") String token) {
    	log.info("Entered into validate token method");
        service.validateToken(token);
        return "Token is valid";
    }
}