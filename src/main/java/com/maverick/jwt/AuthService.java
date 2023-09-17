package com.maverick.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuthService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenService jwtService;
    @Autowired
    AuthenticationManager authManager;

    AuthenticationResponse signup(SignupDto signupdto) {
        var user = User.builder()
                .firstName(signupdto.firstName())
                .lastName(signupdto.lastName())
                .email(signupdto.email())
                .password(passwordEncoder.encode(signupdto.password()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        return new AuthenticationResponse(jwtService.getToken(user));
    }

    AuthenticationResponse signin(SigninDto signindto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(signindto.email(), signindto.password()));
        User user = userRepo.findByEmail(signindto.email()).orElseThrow();
        return new AuthenticationResponse(jwtService.getToken(user));
    }
}