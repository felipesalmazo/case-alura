package br.com.alura.techcase.core.repository;

import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAllByStatus(Status status, Pageable pageable);

    Optional<Course> findByCode(String code);

    Optional<Course> findCourseByStatusAndCode(Status status, String code);
}