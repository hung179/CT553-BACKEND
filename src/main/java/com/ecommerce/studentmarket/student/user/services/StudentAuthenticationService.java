package com.ecommerce.studentmarket.student.user.services;

import com.ecommerce.studentmarket.common.authencation.exceptions.UnauthenticatedException;
import com.ecommerce.studentmarket.common.authencation.dtos.*;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentInvalidatedTokenDomain;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import com.ecommerce.studentmarket.student.user.repositories.StudentInvalidatedTokenRepository;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class StudentAuthenticationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @Autowired
    private StudentInvalidatedTokenRepository studentInvalidatedTokenRepository;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        boolean isValid = true;

        try {
            verifyToken(token);
        }catch (Exception e){
            isValid = false;
        }
        return IntrospectResponse
                .builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request){
        StudentDomain student = studentRepository.findById(request.getUsername()).orElseThrow(
                () -> {
                    return new StudentNotFoundException(request.getUsername());
                }
        );

        boolean authenticated = passwordEncoder.matches(request.getPassword(), student.getPassword());
        if (!authenticated) {
            throw new UnauthenticatedException("Sai mật khẩu hoặc tài khoản");
        }

        String token = generateToken(student);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        StudentInvalidatedTokenDomain invalidatedTokenDomain = StudentInvalidatedTokenDomain.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        studentInvalidatedTokenRepository.save(invalidatedTokenDomain);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))){
            throw new UnauthenticatedException("Lỗi token logout");
        }

        if (studentInvalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new UnauthenticatedException("Lỗi token logout");
        }

        return signedJWT;
    }

    private String generateToken(StudentDomain student){
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            JWTClaimsSet jwtClaimsSet = new  JWTClaimsSet.Builder()
                    .subject(student.getMssv())
                    .issuer("studentmarket-api")
                    .issueTime(new Date())
                    .expirationTime(
                            new Date(Instant
                                    .now()
                                    .plus( 1, ChronoUnit.HOURS)
                                    .toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", student.getRole())
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);


            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }
    }

}
