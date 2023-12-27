package com.example.jwtsecurityapp.auth;

import com.example.jwtsecurityapp.User.Role;
import com.example.jwtsecurityapp.User.User;
import com.example.jwtsecurityapp.User.UserRepository;
import com.example.jwtsecurityapp.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))//se si cripteaza parola
                .role(Role.USER)
                .build();
        repository.save(user);//salvam userul in baza de date
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
            authenticationManager.authenticate(/**Metoda are grija sa mi returneze inclusiv erori daca email ul sau parola nu sunt corecte*/
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
            );

            //totul a fost ok si general un token pe care sa l trimitem inapoi

            var user = repository.findByEmail(request.getEmail()).orElseThrow();

            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                .token(jwtToken).build();
        } catch (AuthenticationException e){
            System.out.println("exception at authentication " + e);
            return AuthenticationResponse.builder().build();
        }
    }
}
