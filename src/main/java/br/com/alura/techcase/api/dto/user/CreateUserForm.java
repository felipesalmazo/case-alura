package br.com.alura.techcase.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserForm(
        @NotBlank(message = "Empty fields will not be accepted.")
        @Size(max = 20, message = "The maximum of characters in the username is 20.")
        @Pattern(regexp = "^[a-z]+$", message = "Only lowercase characters will be accepted.")
        String username,
        @NotBlank(message = "Empty fields will not be accepted.")
        String name,
        @NotBlank(message = "Empty fields will not be accepted.")
        @Email(message = "Only valid emails will be accepted. Ex: email@email.com")
        String email,
        @NotBlank(message = "Empty fields will not be accepted.")
        @Pattern(regexp = "^[A-Z]+$", message = "Only uppercase characters will be accepted.")
        String role,
        @NotBlank(message = "Empty fields will not be accepted.")
        String password
) {
}
