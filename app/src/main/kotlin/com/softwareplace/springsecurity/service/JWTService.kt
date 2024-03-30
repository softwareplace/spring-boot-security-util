package com.softwareplace.springsecurity.service

import com.softwareplace.springsecurity.config.ApplicationInfo
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class JWTService(
    private val applicationInfo: ApplicationInfo,
    private val encoder: JwtEncoder
) {

    fun jwtGenerate(
        subject: String,
        claims: Map<String, List<Any>> = mapOf(),
        expiration: Long = applicationInfo.jwtExpirationTime
    ): String {
        val now = Instant.now()

        val jwtClaimsSet = JwtClaimsSet.builder()
            .issuer(applicationInfo.issuer)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiration))
            .subject(subject)
            .claim("scope", claims)
            .build()

        return encoder.encode(JwtEncoderParameters.from(jwtClaimsSet))
            .tokenValue
    }
}