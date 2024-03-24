package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.course.CourseAssessmentForm;
import br.com.alura.techcase.api.dto.course.CourseDetail;
import br.com.alura.techcase.api.dto.course.CourseNpsDetail;
import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.api.model.CourseAssessment;
import br.com.alura.techcase.core.enums.Role;
import br.com.alura.techcase.core.enums.Status;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.repository.CourseAssessmentRepository;
import br.com.alura.techcase.core.repository.CourseRepository;
import br.com.alura.techcase.core.repository.EnrollmentRepository;
import br.com.alura.techcase.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseAssessmentRepository courseAssessmentRepository;

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
        course.setInactivationDate(LocalDate.now());
        courseRepository.save(course);
    }

    public CourseAssessment courseAssessment (CourseAssessmentForm form) throws NotFoundException, ValidationException {
        var user = userRepository.findByUsername(form.userUsername()).orElseThrow(() -> new NotFoundException("User with username " + form.userUsername() + " not found."));
        var course = courseRepository.findCourseByStatusAndCode(Status.ACTIVE, form.courseCode()).orElseThrow(() -> new NotFoundException("Course with code " + form.courseCode() + " not found or inactive."));
        enrollmentRepository.findEnrollmentByUserAndCourse(user, course).orElseThrow(() -> new ValidationException("The user have to be enrolled to the course to do the assessment."));

        if (form.assessmentGrade() < 6 && (form.description() == null || form.description().isEmpty())) {
            throw new ValidationException("The description must be completed because the grade is equals or lower than 6.");
        }

        var courseAssessment = new CourseAssessment(form, user, course);
        courseAssessmentRepository.save(courseAssessment);

        if (form.assessmentGrade() < 6) {
            String body = String.format("This course received an %d rating from a student, and this was the description of the assessment: \n %s", form.assessmentGrade(), form.description());
            EmailSender.send(user.getEmail(), "Assessment of Course: " + course.getName(), body);
        }

        return courseAssessment;
    }

    public List<CourseNpsDetail> courseNps() throws NotFoundException {
        var npsDataList = courseAssessmentRepository.getNpsDataByCourse();

        if(npsDataList.isEmpty()) {
            throw new NotFoundException("The courses doesn't have sufficient enrolls or assessment grades yet.");
        }

        npsDataList.forEach(item -> {
            item.setNps(((item.getPromoters().doubleValue() - item.getDetractors().doubleValue()) / item.getTotalAssessments().doubleValue()) * 100);
        });

        return npsDataList.stream().map(CourseNpsDetail::new).toList();

    }
}
