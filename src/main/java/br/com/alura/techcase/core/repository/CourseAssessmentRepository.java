package br.com.alura.techcase.core.repository;

import br.com.alura.techcase.api.dto.course.CourseNpsData;
import br.com.alura.techcase.api.model.CourseAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseAssessmentRepository extends JpaRepository<CourseAssessment, Long> {

    @Query(value = """
            SELECT new br.com.alura.techcase.api.dto.course.CourseNpsData(
            courses.name,
            courses.code,
            courses.status,
            SUM(CASE WHEN ca.assessmentGrade BETWEEN 9 AND 10 THEN 1 ELSE 0 END),
            SUM(CASE WHEN ca.assessmentGrade BETWEEN 0 AND 5 THEN 1 ELSE 0 END),
            COUNT(ca.id)
            )
            FROM
                (SELECT e.course.id, e.course.name, e.course.code, e.course.status
                FROM Enrollment e
                GROUP BY e.course.id
                HAVING COUNT(e.id) > 4) AS courses
            JOIN CourseAssessment ca ON ca.course.id = courses.id
            GROUP BY ca.course.id""")
    List<CourseNpsData> getNpsDataByCourse();
}