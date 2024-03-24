package br.com.alura.techcase.api.dto.course;

public record CourseNpsDetail(
       String courseName,
       String courseCode,
       String status,
       Double nps
) {
    public CourseNpsDetail(CourseNpsData data) {
        this(data.getCourseName(), data.getCourseCode(), data.getStatus(), data.getNps());
    }
}
