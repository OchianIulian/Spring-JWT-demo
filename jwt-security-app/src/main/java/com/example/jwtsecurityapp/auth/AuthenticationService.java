package com.example.jwtsecurityapp.auth;

import com.example.jwtsecurityapp.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;

    public AuthenticationResponse register(RegisterRequest request) {
        return  null;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
