package br.com.alura.techcase.api.model;

import br.com.alura.techcase.api.dto.course.CourseAssessmentForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CourseAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private Integer assessmentGrade;
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseAssessment(CourseAssessmentForm form, User userParam, Course courseParam) {
        this.assessmentGrade = form.assessmentGrade();
        this.description = form.description();
        this.user = userParam;
        this.course = courseParam;
    }
}
