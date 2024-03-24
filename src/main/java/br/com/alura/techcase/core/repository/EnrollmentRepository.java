package br.com.alura.techcase.core.repository;

import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.api.model.Enrollment;
import br.com.alura.techcase.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findEnrollmentByUserAndCourse(User user, Course course);
}