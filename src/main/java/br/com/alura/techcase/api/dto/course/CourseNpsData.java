package br.com.alura.techcase.api.dto.course;

import br.com.alura.techcase.core.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseNpsData {

    private String courseName;
    private String courseCode;
    private String status;
    private Long promoters;
    private Long detractors;
    private Long totalAssessments;
    private Long grades;
    private Double nps;

    public CourseNpsData(String courseName, String courseCode, Status status, Long promoters, Long detractors, Long totalAssessments) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.status = status.name();
        this.promoters = promoters;
        this.detractors = detractors;
        this.totalAssessments = totalAssessments;
    }
}
