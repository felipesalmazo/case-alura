package br.com.alura.techcase.api.controller;

import br.com.alura.techcase.api.dto.course.CourseAssessmentForm;
import br.com.alura.techcase.api.dto.course.CourseDetail;
import br.com.alura.techcase.api.dto.course.CreateCourseForm;
import br.com.alura.techcase.api.dto.course.InactivateCourseForm;
import br.com.alura.techcase.core.exception.NotFoundException;
import br.com.alura.techcase.core.exception.ValidationException;
import br.com.alura.techcase.core.service.CourseService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;

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
    public ResponseEntity<?> createCourse(@RequestBody @Valid CreateCourseForm form) throws ValidationException, NotFoundException {
        var course = courseService.createCourse(form);
        var uri = UriComponentsBuilder.newInstance()
                .path("/api/course/create/{id}")
                .buildAndExpand(course.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/inactivate")
    public ResponseEntity<?> inactivateCourse (@RequestBody @Valid InactivateCourseForm form) throws NotFoundException {
        courseService.inactivateCourse(form.code());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assessment")
    public ResponseEntity<?> courseAssessment(@RequestBody @Valid CourseAssessmentForm form) throws ValidationException, NotFoundException {
        var courseAssessment = courseService.courseAssessment(form);
        var uri = UriComponentsBuilder.newInstance()
                .path("/api/course/assessment/{id}")
                .buildAndExpand(courseAssessment.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/nps")
    public ResponseEntity<byte[]> courseNps() throws NotFoundException {
        var npsList = courseService.courseNps();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("NPS Report"));
            document.add(new Paragraph());

            Table table = new Table(4);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.addHeaderCell("Course Name").setWidth(400);
            table.addHeaderCell("Course Code").setWidth(400);
            table.addHeaderCell("Status").setWidth(400);
            table.addHeaderCell("NPS").setWidth(400);

            npsList.forEach((item -> {
                table.addCell(item.courseName());
                table.addCell(item.courseCode());
                table.addCell(item.status());
                table.addCell(String.valueOf(item.nps().intValue()));

            }));

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "npsreport.pdf");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
