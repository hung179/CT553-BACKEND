package com.ecommerce.studentmarket.common.security;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import com.ecommerce.studentmarket.common.authencation.dtos.IntrospectRequest;
import com.ecommerce.studentmarket.common.authencation.dtos.IntrospectResponse;
import com.ecommerce.studentmarket.admin.user.services.AdminAuthenticationService;
import com.ecommerce.studentmarket.student.user.services.StudentAuthenticationService;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signer-key}")
    private String signerKey;

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @Autowired
    private StudentAuthenticationService studentAuthenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            var scope = signedJWT.getJWTClaimsSet().getClaim("scope");

            if (scope == null || scope.toString().trim().isEmpty()) {
                throw new JwtException("Token không tìm thấy scope");
            }

            IntrospectResponse response;
            if (scope.equals("admin")) {
                response = adminAuthenticationService.introspect(
                        IntrospectRequest.builder().token(token).build());
            }else if (scope.equals("student")){
                response = studentAuthenticationService.introspect(
                        IntrospectRequest.builder().token(token).build());
            }else {
                throw new JwtException("Scope không được hỗ trợ: " + scope);
            }
            if (!response.isValid()) throw new JwtException("Token không hợp lệ");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
