package br.com.alura.techcase.api.dto.course;

import br.com.alura.techcase.api.dto.user.UserDetail;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.core.utils.SiteUtil;

public record CourseDetail(
        String name,
        String code,
        UserDetail instructor,
        String status,
        String description,
        String creationDate,
        String inactivationDate
) {
    public CourseDetail(Course course) {
        this(course.getName(), course.getCode(), new UserDetail(course.getInstructor()), course.getStatus().name(), course.getDescription(), SiteUtil.formatDate(course.getCreationDate()), SiteUtil.formatDate(course.getInactivationDate()));
    }
}
