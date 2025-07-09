package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.student.user.dtos.StudentDto;
import com.ecommerce.studentmarket.student.user.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping()
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDto student) {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllStudent(){
            return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/{mssv}")
    public ResponseEntity<?> getStudentById(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.getStudentById(mssv));
    }

    @PatchMapping("/{mssv}")
    public ResponseEntity<?> updateStudentById(@PathVariable String mssv, @RequestBody StudentDto studentDto){
            return ResponseEntity.ok(studentService.updateStudent(mssv , studentDto));
    }

    @DeleteMapping("/{mssv}")
    public  ResponseEntity<?> changeAccountStatus(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.changeAccountStatus(mssv));
    }
}
