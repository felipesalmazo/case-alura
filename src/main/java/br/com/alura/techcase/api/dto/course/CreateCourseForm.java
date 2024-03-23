package br.com.alura.techcase.api.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCourseForm(
        @NotBlank(message = "Empty fields will not be accepted.")
        String name,
        @NotBlank(message = "Empty fields will not be accepted.")
        @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "code cannot have any numbers or special characters. Dashes will be accepted only if between words")
        String code,
        @NotBlank(message = "Empty fields will not be accepted.")
        String instructorUsername,
        String description
) {
}
