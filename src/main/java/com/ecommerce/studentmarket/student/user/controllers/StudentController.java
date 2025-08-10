package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.student.user.dtos.StudentRequestDto;
import com.ecommerce.studentmarket.student.user.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createStudent(@RequestPart("student") StudentRequestDto student,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files) {
            return ResponseEntity.ok(studentService.createStudent(student, files));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> searchStudentByName(
            @RequestParam String tenSV,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(studentService.searchStudentByName(tenSV, page, size));
    }

    @GetMapping("storeId/{maGHDT}")
    public ResponseEntity<?>getStudentByStoreId(@PathVariable Long maGHDT){
        return ResponseEntity.ok(studentService.getStudentByStoreId(maGHDT));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getAllStudent(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
            return ResponseEntity.ok(studentService.getAllStudent(page, size));
    }

    @GetMapping("/{mssv}")
    @PreAuthorize("hasRole('admin') or hasRole('student')")
    public ResponseEntity<?> getStudentById(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.getStudentById(mssv));
    }

    @PatchMapping("/{mssv}")
    @PreAuthorize("hasRole('admin') or #mssv == authentication.name")
    public ResponseEntity<?> updateStudentById(@PathVariable String mssv, @RequestPart("student") StudentRequestDto studentDto,
                                @RequestPart(value = "files", required = false) List<MultipartFile> files){
            return ResponseEntity.ok(studentService.updateStudent(mssv , studentDto, files));
    }

    @DeleteMapping("/{mssv}")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<?> changeAccountStatus(@PathVariable String mssv){
            return ResponseEntity.ok(studentService.changeAccountStatus(mssv));
    }

    @GetMapping("/myInfo")
    @PostAuthorize("returnObject.body.mssv == authentication.name")
    public ResponseEntity<?> getMyInfo() {
        return ResponseEntity.ok(studentService.getMyInfo());
    }

    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(studentService.createStudentsFromExcel(file));
    }

}
