package br.com.alura.techcase.core.service;

import br.com.alura.techcase.api.dto.course.*;
import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.api.model.CourseAssessment;
import br.com.alura.techcase.api.model.Enrollment;
import br.com.alura.techcase.api.model.User;
import br.com.alura.techcase.core.enums.Status;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.repository.CourseAssessmentRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseAssessmentRepository courseAssessmentRepository;

    @InjectMocks
    private CourseService courseService;

    private User user;
    private User instructor;
    private Course course;
    private CreateCourseForm form;
    private CreateCourseForm wrongForm;
    private Enrollment enrollment;
    private CourseAssessmentForm courseAssessmentForm;
    private CourseAssessment courseAssessment;
    private CourseAssessmentForm courseAssessmentFormLowerThanSixDescNull;
    private CourseAssessmentForm courseAssessmentFormLowerThanSixDescEmpty;

    @BeforeEach
    void setUp() {
        form = new CreateCourseForm("course", "crs", "usertestinst", "test");
        course = new Course(form, instructor);

        wrongForm = new CreateCourseForm("course", "crs", "usertest", "test");

        var creteUserForm = new CreateUserForm("usertest", "user test", "email@email.com", "ADMIN", "12345");
        user = new User(creteUserForm, creteUserForm.password());

        var creteUserFormInstructor = new CreateUserForm("usertestinst", "user test", "email@email.com", "INSTRUCTOR", "12345");
        instructor = new User(creteUserFormInstructor, creteUserFormInstructor.password());

        enrollment = new Enrollment(user, course);

        courseAssessmentForm = new CourseAssessmentForm(10, "", "crs");
        courseAssessment = new CourseAssessment(courseAssessmentForm, instructor, course);

        courseAssessmentFormLowerThanSixDescEmpty = new CourseAssessmentForm(5, "", "crs");
        courseAssessmentFormLowerThanSixDescNull = new CourseAssessmentForm(5, null, "crs");
    }

    @Test
    void shouldListByActive() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));

        Page<Course> coursePage = new PageImpl<>(courses, PageRequest.of(0, 5), courses.size());

        when(courseRepository.findAllByStatus(Status.ACTIVE, Pageable.ofSize(5))).thenReturn(coursePage);

        var coursesListFromRep = assertDoesNotThrow(() -> courseService.list(Status.ACTIVE.name(), Pageable.ofSize(5)));
        var courseDetailsList = courses.stream().map(CourseDetail::new).toList();

        assertEquals(courseDetailsList, coursesListFromRep.stream().toList());
        verify(courseRepository, times(1)).findAllByStatus(Status.ACTIVE, Pageable.ofSize(5));
    }

    @Test
    void shouldListByInactive() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));
        courses.add(new Course(form, instructor));

        courses.forEach(item -> item.setStatus(Status.INACTIVE));

        Page<Course> coursePage = new PageImpl<>(courses, PageRequest.of(0, 5), courses.size());

        when(courseRepository.findAllByStatus(Status.INACTIVE, Pageable.ofSize(5))).thenReturn(coursePage);

        var coursesListFromRep = assertDoesNotThrow(() -> courseService.list(Status.INACTIVE.name(), Pageable.ofSize(5)));
        var courseDetailsList = courses.stream().map(CourseDetail::new).toList();

        assertEquals(courseDetailsList, coursesListFromRep.stream().toList());
        verify(courseRepository, times(1)).findAllByStatus(Status.INACTIVE, Pageable.ofSize(5));
    }

    @Test
    void shouldListByStatusEmpty() {
        List<Course> coursesActive = new ArrayList<>();
        coursesActive.add(new Course(form, instructor));
        coursesActive.add(new Course(form, instructor));

        List<Course> coursesInactive = new ArrayList<>();
        coursesInactive.add(new Course(form, instructor));
        coursesInactive.add(new Course(form, instructor));

        coursesInactive.forEach(item -> item.setStatus(Status.INACTIVE));
        coursesActive.forEach(item -> item.setStatus(Status.ACTIVE));

        List<Course> courses = new ArrayList<>();
        courses.addAll(coursesActive);
        courses.addAll(coursesInactive);

        Page<Course> coursePage = new PageImpl<>(courses, PageRequest.of(0, 4), courses.size());

        when(courseRepository.findAll(Pageable.ofSize(4))).thenReturn(coursePage);

        var coursesListFromRep = assertDoesNotThrow(() -> courseService.list("", Pageable.ofSize(4)));
        var courseDetailsList = courses.stream().map(CourseDetail::new).toList();

        assertEquals(courseDetailsList, coursesListFromRep.stream().toList());
        verify(courseRepository, times(1)).findAll(Pageable.ofSize(4));
    }

    @Test
    void shouldListByStatusNull() {
        List<Course> coursesActive = new ArrayList<>();
        coursesActive.add(new Course(form, instructor));
        coursesActive.add(new Course(form, instructor));

        List<Course> coursesInactive = new ArrayList<>();
        coursesInactive.add(new Course(form, instructor));
        coursesInactive.add(new Course(form, instructor));

        coursesInactive.forEach(item -> item.setStatus(Status.INACTIVE));
        coursesActive.forEach(item -> item.setStatus(Status.ACTIVE));

        List<Course> courses = new ArrayList<>();
        courses.addAll(coursesActive);
        courses.addAll(coursesInactive);

        Page<Course> coursePage = new PageImpl<>(courses, PageRequest.of(0, 4), courses.size());

        when(courseRepository.findAll(Pageable.ofSize(4))).thenReturn(coursePage);

        var coursesListFromRep = assertDoesNotThrow(() -> courseService.list(null, Pageable.ofSize(4)));
        var courseDetailsList = courses.stream().map(CourseDetail::new).toList();

        assertEquals(courseDetailsList, coursesListFromRep.stream().toList());
        verify(courseRepository, times(1)).findAll(Pageable.ofSize(4));
    }

    @Test
    void shouldCreateCourse() {
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.save(course)).thenReturn(course);

        var newCourse = assertDoesNotThrow(() -> courseService.createCourse(form));

        ArgumentCaptor<Course> userArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals(userArgumentCaptor.getValue(), newCourse);
    }

    @Test
    void shouldNotCreateCourseBecauseUserIsNull() {
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.empty());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> courseService.createCourse(form));
        assertThat(ex.getMessage(), is("User with username usertestinst not found."));
        verifyNoInteractions(courseRepository);
    }

    @Test
    void shouldNotCreateCourseBecauseUserIsNotInstructor() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        final ValidationException ex = assertThrows(ValidationException.class, () -> courseService.createCourse(wrongForm));
        assertThat(ex.getMessage(), is("The selected user is not an instructor."));
        verifyNoInteractions(courseRepository);
    }

    @Test
    void shouldInactivateCourse() {
        when(courseRepository.findByCode("crs")).thenReturn(Optional.of(course));

        assertDoesNotThrow(() -> courseService.inactivateCourse(form.code()));
        ArgumentCaptor<Course> userArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository, times(1)).save(userArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue(), course);
    }

    @Test
    void shouldNotInactivateCourseBecauseCourseIsNull() {
        when(courseRepository.findByCode("crs")).thenReturn(Optional.empty());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> courseService.inactivateCourse(form.code()));
        assertThat(ex.getMessage(), is("Course with code crs not found."));
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void shouldCreateCourseAssessment() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(instructor, course)).thenReturn(Optional.of(enrollment));
        when(courseAssessmentRepository.findByUserAndCourse(instructor, course)).thenReturn(Optional.empty());

        var newCourseAssessment = assertDoesNotThrow(() -> courseService.courseAssessment(courseAssessmentForm));

        ArgumentCaptor<CourseAssessment> userArgumentCaptor = ArgumentCaptor.forClass(CourseAssessment.class);
        verify(courseAssessmentRepository, times(1)).save(userArgumentCaptor.capture());

        assertEquals(userArgumentCaptor.getValue(), newCourseAssessment);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseUserIsNull() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.empty());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> courseService.courseAssessment(courseAssessmentForm));
        assertThat(ex.getMessage(), is("User with username usertestinst not found."));
        verifyNoInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseCourseIsNullOrInactive() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.empty());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> courseService.courseAssessment(courseAssessmentForm));
        assertThat(ex.getMessage(), is("Course with code crs not found or inactive."));
        verifyNoInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseUserIsNotEnrolled() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(instructor, course)).thenReturn(Optional.empty());

        final ValidationException ex = assertThrows(ValidationException.class, () -> courseService.courseAssessment(courseAssessmentForm));
        assertThat(ex.getMessage(), is("The user have to be enrolled to the course to do the assessment."));
        verifyNoInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseUserAlreadyAssessed() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(instructor, course)).thenReturn(Optional.of(enrollment));
        when(courseAssessmentRepository.findByUserAndCourse(instructor, course)).thenReturn(Optional.of(courseAssessment));

        final ValidationException ex = assertThrows(ValidationException.class, () -> courseService.courseAssessment(courseAssessmentForm));
        assertThat(ex.getMessage(), is("The user have already assessed this course."));
        verifyNoMoreInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseAssessmentGradeIsLowerThanSixAndDescriptionIsNull() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(instructor, course)).thenReturn(Optional.of(enrollment));
        when(courseAssessmentRepository.findByUserAndCourse(instructor, course)).thenReturn(Optional.empty());

        final ValidationException ex = assertThrows(ValidationException.class, () -> courseService.courseAssessment(courseAssessmentFormLowerThanSixDescNull));
        assertThat(ex.getMessage(), is("The description must be completed because the grade is equals or lower than 6."));
        assertNull(courseAssessmentFormLowerThanSixDescNull.description());
        verifyNoMoreInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotCreateCourseAssessmentBecauseAssessmentGradeIsLowerThanSixAndDescriptionIsEmpty() {
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken("usertestinst", "test");

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationMock);
        when(userRepository.findByUsername(instructor.getUsername())).thenReturn(Optional.of(instructor));
        when(courseRepository.findCourseByStatusAndCode(Status.ACTIVE, "crs")).thenReturn(Optional.of(course));
        when(enrollmentRepository.findEnrollmentByUserAndCourse(instructor, course)).thenReturn(Optional.of(enrollment));
        when(courseAssessmentRepository.findByUserAndCourse(instructor, course)).thenReturn(Optional.empty());

        final ValidationException ex = assertThrows(ValidationException.class, () -> courseService.courseAssessment(courseAssessmentFormLowerThanSixDescEmpty));
        assertThat(ex.getMessage(), is("The description must be completed because the grade is equals or lower than 6."));
        assertEquals(courseAssessmentFormLowerThanSixDescEmpty.description(), "");
        verifyNoMoreInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldGetCourseNps() {
        List<CourseNpsData> courseNpsDataList = new ArrayList<>();
        courseNpsDataList.add(new CourseNpsData());

        courseNpsDataList.forEach(item -> {
            item.setCourseName(course.getName());
            item.setCourseCode(course.getCode());
            item.setPromoters(5L);
            item.setDetractors(3L);
            item.setTotalAssessments(10L);
        });

        when(courseAssessmentRepository.getNpsDataByCourse()).thenReturn(courseNpsDataList);

        var npsDetailListFromService = assertDoesNotThrow(() -> courseService.courseNps());
        var npsDetailListMock = courseNpsDataList.stream().map(CourseNpsDetail::new).toList();

        assertEquals(npsDetailListFromService, npsDetailListMock);
        verifyNoMoreInteractions(courseAssessmentRepository);
    }

    @Test
    void shouldNotGetCourseNpsBecauseListIsEmpty() {
        when(courseAssessmentRepository.getNpsDataByCourse()).thenReturn(new ArrayList<>());

        final NotFoundException ex = assertThrows(NotFoundException.class, () -> courseService.courseNps());
        assertThat(ex.getMessage(), is("The courses doesn't have sufficient enrolls or assessment grades yet."));
        verifyNoMoreInteractions(courseAssessmentRepository);
    }
}