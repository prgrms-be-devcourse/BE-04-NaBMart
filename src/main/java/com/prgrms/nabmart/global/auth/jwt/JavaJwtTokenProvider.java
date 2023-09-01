package com.prgrms.nabmart.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.service.response.RegisterUserResponse;
import com.prgrms.nabmart.global.auth.jwt.dto.Claims;
import com.prgrms.nabmart.global.auth.exception.InvalidJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
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
            @Value("${jwt.issuer}") final String issuer,
            @Value("${jwt.client-secret}") final String clientSecret,
            @Value("${jwt.expiry-seconds}") final int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    @Override
    public String createToken(final RegisterUserResponse userResponse) {
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

    @Override
    public Claims validateToken(final String accessToken) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
            Long userId = getUserId(decodedJWT);
            List<String> authorities = getAuthorities(decodedJWT);
            return new Claims(userId, authorities);
        } catch (AlgorithmMismatchException ex) {
            log.info("AlgorithmMismatchException: 토큰의 알고리즘이 유효하지 않습니다.");
        } catch (SignatureVerificationException ex) {
            log.info("SignatureVerificationException: 토큰의 서명이 유효하지 않습니다.");
        } catch (TokenExpiredException ex) {
            log.info("TokenExpiredException: 토큰이 만료되었습니다.");
        } catch (JWTVerificationException ex) {
            log.info("JWTVerificationException: 유효하지 않은 토큰입니다.");
        }
        throw new InvalidJwtException("유효하지 않은 토큰입니다.");
    }

    private Long getUserId(final DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(USER_ID);
        if (!claim.isNull()) {
            return claim.asLong();
        }
        throw new MissingClaimException(USER_ID);
    }

    private List<String> getAuthorities(final DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(ROLE);
        if (!claim.isNull()) {
            String role = claim.asString();
            return UserRole.valueOf(role).getAuthorities();
        }
        throw new MissingClaimException(ROLE);
    }
}
