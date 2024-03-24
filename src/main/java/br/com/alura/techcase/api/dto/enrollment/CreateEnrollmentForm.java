package br.com.alura.techcase.api.dto.enrollment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateEnrollmentForm(
        @NotBlank(message = "Empty fields will not be accepted.")
        @Size(max = 20, message = "The maximum of characters in the username is 20.")
        @Pattern(regexp = "^[a-z]+$", message = "Only lowercase characters will be accepted.")
        String userUsername,
        @NotBlank(message = "Empty fields will not be accepted.")
        @Size(max = 10, message = "The maximum number of characters is 10,")
        @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Code cannot have any numbers or special characters. Dashes will be accepted only if between words.")
        String courseCode
) {
}
