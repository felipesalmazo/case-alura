package br.com.alura.techcase.core.repository;

import br.com.alura.techcase.api.dto.course.CourseNpsData;
import br.com.alura.techcase.api.model.CourseAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseAssessmentRepository extends JpaRepository<CourseAssessment, Long> {

    @Query("""
            SELECT new br.com.alura.techcase.api.dto.course.CourseNpsData(
            c.name,
            c.code,
            c.status,
            SUM(CASE WHEN ca.assessmentGrade BETWEEN 9 AND 10 THEN 1 ELSE 0 END),
            SUM(CASE WHEN ca.assessmentGrade BETWEEN 0 AND 5 THEN 1 ELSE 0 END),
            COUNT(ca)
            )
            FROM
                Course c
                    LEFT JOIN CourseAssessment ca ON c.id = ca.course.id
                    LEFT JOIN Enrollment e ON c.id = e.course.id
            GROUP BY
                c.id
            HAVING
                COUNT(DISTINCT e.id) > 4""")
    List<CourseNpsData> getNpsDataByCourse();
}