package br.com.alura.techcase.api.controller;

import br.com.alura.techcase.api.dto.authentication.AuthenticationResponse;
import br.com.alura.techcase.api.dto.authentication.LoginForm;
import br.com.alura.techcase.core.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginForm loginForm) {
       return ResponseEntity.ok(authenticationService.login(loginForm));
    }
}
