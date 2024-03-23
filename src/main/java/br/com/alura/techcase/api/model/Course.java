package br.com.alura.techcase.api.model;

import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.core.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User instructor;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private Status status;

    @Column(nullable = false)
    private Date creationDate;
    private Date inactivationDate;

    public Course(CreateCourseForm form, User instrutor) {
        this.name = form.name();
        this.code = form.code();
        this.instructor = instrutor;
        this.description = form.description();
        this.status = Status.ACTIVE;
        this.creationDate = new Date();
        this.inactivationDate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
