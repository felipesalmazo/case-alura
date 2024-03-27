package br.com.alura.techcase.core.repository;

import br.com.alura.techcase.api.dto.course.CourseNpsData;
import br.com.alura.techcase.api.model.Course;
import br.com.alura.techcase.api.model.CourseAssessment;
import br.com.alura.techcase.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseAssessmentRepository extends JpaRepository<CourseAssessment, Long> {

    @Query(value = """
            SELECT new br.com.alura.techcase.api.dto.course.CourseNpsData(
                courses.name,
                courses.code,
                courses.status,
                SUM(CASE WHEN ca.assessmentGrade BETWEEN 9 AND 10 THEN 1 ELSE 0 END),
                SUM(CASE WHEN ca.assessmentGrade BETWEEN 0 AND 5 THEN 1 ELSE 0 END),
                COUNT(ca.id)
            ) AS table
            FROM (
                SELECT e.course.id AS course_id, e.course.name AS name, e.course.code AS code, e.course.status AS status
                FROM Enrollment AS e
                GROUP BY e.course.id
                HAVING COUNT(e.id) > 4
            ) AS courses
            JOIN CourseAssessment AS ca ON ca.course.id = courses.course_id
            GROUP BY courses.course_id""")
    List<CourseNpsData> getNpsDataByCourse();

    
}