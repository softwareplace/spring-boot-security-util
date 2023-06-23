package com.softwareplace.springsecurity.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.softwareplace.jsonlogger.log.JsonLog
import com.softwareplace.jsonlogger.log.kLogger
import com.softwareplace.springsecurity.config.ApplicationInfo
import com.softwareplace.springsecurity.exception.UnauthorizedAccessExceptionApi
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.event.Level
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTSystem(private val applicationInfo: ApplicationInfo) {

    fun jwtGenerate(
        subject: String,
        claims: Map<String, List<Any>> = mapOf(),
        expiration: Long = applicationInfo.jwtExpirationTime
    ): String {
        val algorithm = Algorithm.HMAC512(applicationInfo.securitySecret)
        val currentTimeMillis = System.currentTimeMillis()

        val builder = JWT.create()
            .withSubject(subject)

        claims.forEach { (key, value) -> builder.withClaim(key, value) }

        return builder
            .withIssuedAt(Date(currentTimeMillis))
            .withJWTId(UUID.randomUUID().toString())
            .withExpiresAt(Date(currentTimeMillis + expiration))
            .sign(algorithm)
    }

    fun getJwt(request: HttpServletRequest): DecodedJWT? {
        val jwt = request.getJwt()
            ?.replace(BEARER, "")
            ?.replace(BASIC, "")
        return decodedJWT(jwt)
    }

    fun decodedJWT(jwt: String?): DecodedJWT? {
        try {
            val algorithm = Algorithm.HMAC512(applicationInfo.securitySecret)
            val verifier: JWTVerifier = JWT.require(algorithm).build()
            return verifier.verify(jwt)
        } catch (exception: Exception) {
            JsonLog(kLogger)
                .level(Level.ERROR)
                .message("[JWT_ERROR]")
                .add("jwt", jwt ?: "")
                .error(exception)
                .run()

            val errorMessage = when (exception) {
                is JWTVerificationException -> exception.message
                is NullPointerException -> {
                    throw UnauthorizedAccessExceptionApi(
                        message = "Access denied",
                        status = HttpStatus.UNAUTHORIZED,
                        cause = exception
                    )
                }

                else -> "Failed to complete the request;"
            }

            throw UnauthorizedAccessExceptionApi(
                message = errorMessage ?: "Invalid authorization signature token",
                status = HttpStatus.FORBIDDEN,
                cause = exception
            )
        }
    }

    fun getScopes(decodedJWT: DecodedJWT?): List<String> {
        val claim: Claim? = decodedJWT?.getClaim(SCOPES)
        if (claim != null) {
            return claim.asList(String::class.java)
        }
        return emptyList()
    }

    companion object {
        fun HttpServletRequest.getJwt(): String? {
            return getHeader("Authorization") ?: return getHeader("authorization")
        }

        const val JWT_KEY = "jwt"
        const val SCOPES = "scopes"
        private const val BEARER = "Bearer "
        private const val BASIC = "Basic "
    }
}
