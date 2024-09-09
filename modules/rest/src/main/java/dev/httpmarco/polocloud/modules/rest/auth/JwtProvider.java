package dev.httpmarco.polocloud.modules.rest.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtProvider {

    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;
    private final JWTGenerator<TokenInformation> jwtGenerator;

    public JwtProvider(String secret) {
        this.jwtAlgorithm = Algorithm.HMAC512(secret);
        this.jwtVerifier = JWT.require(this.jwtAlgorithm).build();
        this.jwtGenerator = createJwtGenerator();
    }

    private JWTGenerator<TokenInformation> createJwtGenerator() {
        return (tokenInformation, alg) -> {
            JWTCreator.Builder tokenBuilder = JWT.create()
                    .withClaim("ip", tokenInformation.ip())
                    .withClaim("userAgent", tokenInformation.userAgent())
                    .withClaim("uuid", tokenInformation.userUUID().toString())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)));

            return tokenBuilder.sign(alg);
        };
    }
    public JWTProvider<TokenInformation> provider() {
        return new JWTProvider<>(jwtAlgorithm, jwtGenerator, jwtVerifier);
    }

}
