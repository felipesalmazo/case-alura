package br.com.alura.techcase.api.controller;

import br.com.alura.techcase.api.dto.enrollment.CreateEnrollmentForm;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/create")
    public ResponseEntity createEnrollment(@RequestBody @Valid CreateEnrollmentForm form) throws ValidationException, NotFoundException {
        var enrollment = enrollmentService.createEnrollment(form);
        var uri = UriComponentsBuilder.newInstance()
                .path("/api/enrollment/create/{id}")
                .buildAndExpand(enrollment.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
