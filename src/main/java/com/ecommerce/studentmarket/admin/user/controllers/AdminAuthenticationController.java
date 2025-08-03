package com.ecommerce.studentmarket.admin.user.controllers;

import com.ecommerce.studentmarket.admin.user.services.AdminAuthenticationService;
import com.ecommerce.studentmarket.common.authencation.dtos.*;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("admin/auth")
public class AdminAuthenticationController {

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request
            ) {
        AuthenticationResponse response =  adminAuthenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> authenticate (
            @RequestBody IntrospectRequest request
    ) throws ParseException, JOSEException {
        IntrospectResponse response =  adminAuthenticationService.introspect(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (
            @RequestBody LogoutRequest request
    ) throws ParseException, JOSEException {
        adminAuthenticationService.logout(request);
        return ResponseEntity.ok("Đăng xuất thành công");
    }

}
