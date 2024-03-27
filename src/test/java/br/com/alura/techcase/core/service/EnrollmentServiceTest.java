package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.api.dto.enrollment.CreateEnrollmentForm;
import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.api.model.Enrollment;
import br.com.alura.techcase.api.model.User;
import br.com.alura.techcase.core.enums.Status;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.repository.CourseRepository;
import br.com.alura.techcase.core.repository.EnrollmentRepository;
import br.com.alura.techcase.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private User user;
    private Course course;
    private Enrollment enrollment;
    private CreateEnrollmentForm createEnrollmentForm;

    @BeforeEach
    void setUp() {
        var createCourseForm = new CreateCourseForm("course", "crs", "usertest", "test");
        course = new Course(createCourseForm, user);
        course.setStatus(Status.ACTIVE);

        var creteUserForm = new CreateUserForm("usertestinst", "user test", "email@email.com", "INSTRUCTOR", "12345");
        user = new User(creteUserForm, creteUserForm.password());

        createEnrollmentForm = new CreateEnrollmentForm("crs");
        enrollment = new Enrollment(user, course);
    }

    @Test
    void shouldCreateEnrollment() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);


        var newEnrollment = assertDoesNotThrow(() ->enrollmentService.createEnrollment(createEnrollmentForm));

        ArgumentCaptor<Enrollment> userArgumentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals(userArgumentCaptor.getValue(), newEnrollment);
    }

    @Test
    void shouldNotCreateEnrollmentBecauseCourseIsNull() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> enrollmentService.createEnrollment(createEnrollmentForm));
        assertThat(ex.getMessage(),is("Course with code crs not found or inactive."));
        verifyNoInteractions(enrollmentRepository);
    }

    @Test
    void shouldNotCreateEnrollmentBecauseUserIsAlreadyEnrolled() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(user, course)).thenReturn(Optional.of(enrollment));

        final ValidationException ex = assertThrows(ValidationException.class, () -> enrollmentService.createEnrollment(createEnrollmentForm));
        assertThat(ex.getMessage(),is("This user is already enrolled in this course"));
        verifyNoMoreInteractions(enrollmentRepository);
    }

    @Test
    void shouldNotCreateEnrollmentBecauseUserIsNull() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> enrollmentService.createEnrollment(createEnrollmentForm));
        assertThat(ex.getMessage(),is("User with username usertestinst not found."));
        verifyNoInteractions(enrollmentRepository);
    }

}