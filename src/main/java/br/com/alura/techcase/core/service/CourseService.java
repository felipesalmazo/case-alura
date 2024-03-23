package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.course.CourseDetail;
import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.core.enums.Role;
import br.com.alura.techcase.core.enums.Status;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.repository.CourseRepository;
import br.com.alura.techcase.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CourseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    public Page<CourseDetail> list (String status, Pageable pageable) throws ValidationException {

        if (status != null && !status.isEmpty()){
            if(status.equalsIgnoreCase(Status.ACTIVE.name())){
                return courseRepository.findAllByStatus(Status.ACTIVE,pageable).map(CourseDetail::new);
            } else if (status.equalsIgnoreCase(Status.INACTIVE.name())){
                return courseRepository.findAllByStatus(Status.INACTIVE,pageable).map(CourseDetail::new);
            } else {
                throw new ValidationException("The status is not Valid.");
            }
        }

        return courseRepository.findAll(pageable).map(CourseDetail::new);

    }

    public Course createCourse(CreateCourseForm form) throws ValidationException, NotFoundException {
        var user = userRepository.findByUsername(form.instructorUsername()).orElseThrow(() -> new NotFoundException("User with username " + form.instructorUsername() + " not found."));
        if (!user.getRole().name().equals(Role.INSTRUCTOR.name())){
            throw new ValidationException("The selected user is not an instructor.");
        }
        var course = new Course(form, user);
        return courseRepository.save(course);
    }

    public void inactivateCourse(String code) throws NotFoundException {
        var course = courseRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Course with code " + code + " not found."));
        course.setStatus(Status.INACTIVE);
        course.setInactivationDate(new Date());
        courseRepository.save(course);
    }
}
