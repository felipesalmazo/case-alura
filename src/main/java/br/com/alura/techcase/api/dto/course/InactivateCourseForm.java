package br.com.alura.techcase.api.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InactivateCourseForm(
        @NotBlank(message = "Empty fields will not be accepted.")
        @Size(max = 10, message = "The maximum number of characters is 10,")
        @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "code cannot have any numbers or special characters. Dashes will be accepted only if between words")
        String code
) {
}
