package dev.httpmarco.polocloud.modules.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider
import java.util.Date
import java.util.concurrent.TimeUnit

class JwtProvider(secret: String) {

    private val jwtAlgorithm: Algorithm = Algorithm.HMAC512(secret)
    private val jwtVerifier: JWTVerifier = JWT.require(jwtAlgorithm).build()
    private val jwtGenerator: JWTGenerator<TokenInformation> = createJwtGenerator()

    private fun createJwtGenerator(): JWTGenerator<TokenInformation> =
        JWTGenerator { tokenInformation, alg ->
            val tokenBuilder = JWT.create()
                .withClaim("ip", tokenInformation.ip)
                .withClaim("userAgent", tokenInformation.userAgent)
                .withClaim("uuid", tokenInformation.userUUID.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)))

            tokenBuilder.sign(alg)
        }

    fun provider(): JWTProvider<TokenInformation> = JWTProvider(this.jwtAlgorithm, this.jwtGenerator, this.jwtVerifier)
}