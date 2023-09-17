package com.maverick.jwt;

import org.springframework.web.bind.annotation.RestController;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController @NoArgsConstructor @RequestMapping("auth")
public class AuthController{
    @Autowired
    AuthService authService;

    @PostMapping("signup")
    ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupDto signupDto){
        return ResponseEntity.ok(authService.signup(signupDto));
    }
    @PostMapping("signin")
    ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninDto signinDto){
        return ResponseEntity.ok(authService.signin(signinDto));
    }
}