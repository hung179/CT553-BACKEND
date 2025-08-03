package com.ecommerce.studentmarket.admin.services;

import com.ecommerce.studentmarket.admin.domains.AdminDomain;
import com.ecommerce.studentmarket.admin.domains.AdminInvalidatedTokenDomain;
import com.ecommerce.studentmarket.admin.exceptions.AdminNotFoundException;
import com.ecommerce.studentmarket.common.authencation.exceptions.UnauthenticatedException;
import com.ecommerce.studentmarket.admin.repositories.AdminRepository;
import com.ecommerce.studentmarket.admin.repositories.AdminInvalidatedTokenRepository;
import com.ecommerce.studentmarket.common.authencation.dtos.*;
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
public class AdminAuthenticationService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @Autowired
    private AdminInvalidatedTokenRepository adminInvalidatedTokenRepository;

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
        AdminDomain admin = adminRepository.findById(request.getUsername()).orElseThrow(
                () -> {
                    return new AdminNotFoundException(request.getUsername());
                }
        );

        boolean authenticated = passwordEncoder.matches(request.getPassword(), admin.getPassword());
        if (!authenticated) {
            throw new UnauthenticatedException("Sai mật khẩu hoặc tài khoản");
        }

        String token = generateToken(admin);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        AdminInvalidatedTokenDomain invalidatedTokenDomain = AdminInvalidatedTokenDomain.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        adminInvalidatedTokenRepository.save(invalidatedTokenDomain);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))){
            throw new UnauthenticatedException("Lỗi token logout");
        }

        if (adminInvalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new UnauthenticatedException("Lỗi token logout");
        }

        return signedJWT;
    }

    private String generateToken(AdminDomain admin){
        try {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new  JWTClaimsSet.Builder()
                .subject(admin.getMscb())
                .issuer("studentmarket-api")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant
                                .now()
                                .plus( 1, ChronoUnit.HOURS)
                                .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", admin.getRole())
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
