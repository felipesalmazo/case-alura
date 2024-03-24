package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.enrollment.CreateEnrollmentForm;
import br.com.alura.techcase.api.model.Enrollment;
import br.com.alura.techcase.core.enums.Status;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.repository.CourseRepository;
import br.com.alura.techcase.core.repository.EnrollmentRepository;
import br.com.alura.techcase.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment createEnrollment(CreateEnrollmentForm form) throws NotFoundException, ValidationException {
        var user = userRepository.findByUsername(form.userUsername()).orElseThrow(() -> new NotFoundException("User with username " + form.userUsername() + " not found."));
        var course = courseRepository.findCourseByStatusAndCode(Status.ACTIVE, form.courseCode()).orElseThrow(() -> new NotFoundException("Course with code " + form.courseCode() + " not found or inactive."));
        var enrollment = enrollmentRepository.findEnrollmentByUserAndCourse(user, course);

        if (enrollment.isPresent()) {
            throw new ValidationException("This user is already enrolled in this course");
        }

        var newEnrollment = new Enrollment(user, course);
        return enrollmentRepository.save(newEnrollment);
    }
}
