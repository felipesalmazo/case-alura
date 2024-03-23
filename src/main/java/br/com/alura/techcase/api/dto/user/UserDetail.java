package br.com.alura.techcase.api.dto.user;

import br.com.alura.techcase.api.model.User;

public record UserDetail(
        String name,
        String email,
        String role
) {
    public UserDetail(User user) {
        this(user.getName(), user.getEmail(), user.getRole().name());
    }
}
