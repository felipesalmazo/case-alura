package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.authentication.AuthenticationResponse;
import br.com.alura.techcase.api.dto.authentication.LoginForm;
import br.com.alura.techcase.core.config.TokenService;
import br.com.alura.techcase.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public AuthenticationResponse login(LoginForm loginForm) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.username(),
                        loginForm.password()
                )
        );
        var user = userRepository.findByUsername(loginForm.username()).orElseThrow();
        var jwtToken = tokenService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }
}
