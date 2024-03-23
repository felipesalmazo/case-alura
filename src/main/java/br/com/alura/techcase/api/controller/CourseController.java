package br.com.alura.techcase.api.controller;

import br.com.alura.techcase.api.dto.course.CourseDetail;
import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public ResponseEntity<Page<CourseDetail>> list(@RequestParam String status, @PageableDefault(size = 5) Pageable pages) throws ValidationException {
        return ResponseEntity.ok(courseService.list(status, pages));
    }

    @PostMapping("/create")
    public ResponseEntity createCourse(@RequestBody @Valid CreateCourseForm form) throws ValidationException, NotFoundException {
        var course = courseService.createCourse(form);
        var uri = UriComponentsBuilder.newInstance()
                .path("/api/course/create/{id}")
                .buildAndExpand(course.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/inactivate")
    public ResponseEntity inactivateCourse (@RequestBody String code) throws NotFoundException {
        courseService.inactivateCourse(code);
        return ResponseEntity.ok().build();
    }
}
