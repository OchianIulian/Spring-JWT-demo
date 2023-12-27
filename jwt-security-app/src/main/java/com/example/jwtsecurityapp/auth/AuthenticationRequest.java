package com.example.jwtsecurityapp.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
