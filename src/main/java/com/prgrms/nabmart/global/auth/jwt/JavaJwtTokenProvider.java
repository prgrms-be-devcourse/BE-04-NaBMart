package com.prgrms.nabmart.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JavaJwtTokenProvider implements TokenProvider {

    private static final String USER_ID = "userId";
    private static final String ROLE = "role";

    private final String issuer;
    private final String clientSecret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JavaJwtTokenProvider(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.client-secret}") String clientSecret,
            @Value("${jwt.expiry-seconds}") int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    @Override
    public String createToken(RegisterUserResponse userResponse) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirySeconds * 1000L);
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim(USER_ID, userResponse.userId())
                .withClaim(ROLE, userResponse.userRole().getValue())
                .sign(algorithm);
    }
}
