package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.student.user.dtos.StudentRequestDto;
import com.ecommerce.studentmarket.student.user.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequestDto student) {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllStudent(){
            return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/{mssv}")
    @PreAuthorize("hasRole('admin', 'student')")
    public ResponseEntity<?> getStudentById(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.getStudentById(mssv));
    }

    @PatchMapping("/{mssv}")
    @PreAuthorize("hasRole('admin') or #mssv == authentication.name")
    public ResponseEntity<?> updateStudentById(@PathVariable String mssv, @RequestBody StudentRequestDto studentDto){
            return ResponseEntity.ok(studentService.updateStudent(mssv , studentDto));
    }

    @DeleteMapping("/{mssv}")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<?> changeAccountStatus(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.changeAccountStatus(mssv));
    }
}
