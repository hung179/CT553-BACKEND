package com.ecommerce.studentmarket.student.user.controllers;

import com.ecommerce.studentmarket.common.authencation.dtos.*;
import com.ecommerce.studentmarket.student.user.services.StudentAuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("students/auth")
public class StudentAuthenticationController {

    @Autowired
    private StudentAuthenticationService studentAuthenticationService;

    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request
            ) {
        AuthenticationResponse response =  studentAuthenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> authenticate (
            @RequestBody IntrospectRequest request
    ) throws ParseException, JOSEException {
        IntrospectResponse response =  studentAuthenticationService.introspect(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (
            @RequestBody LogoutRequest request
    ) throws ParseException, JOSEException {
        studentAuthenticationService.logout(request);
        return ResponseEntity.ok("Đăng xuất thành công");
    }

}
