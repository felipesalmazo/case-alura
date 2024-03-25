package br.com.alura.techcase.api.dto.authentication;

public record LoginForm(
        String email,
        String password
) {
}
