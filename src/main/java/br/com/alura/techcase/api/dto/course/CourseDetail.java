package br.com.alura.techcase.api.dto.course;

import br.com.alura.techcase.api.dto.user.UserDetail;
import br.com.alura.techcase.api.model.Course;

import java.util.Date;

public record CourseDetail(
        String name,
        String code,
        UserDetail instructor,
        String status,
        String description,
        Date creationDate,
        Date inactivationDate
) {
    public CourseDetail(Course course) {
        this(course.getName(), course.getCode(), new UserDetail(course.getInstructor()), course.getStatus().name(), course.getDescription(), course.getCreationDate(), course.getInactivationDate());
    }
}
