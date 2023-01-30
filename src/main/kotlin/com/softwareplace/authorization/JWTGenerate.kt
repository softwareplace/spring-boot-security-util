package com.softwareplace.authorization

import com.softwareplace.config.ApplicationInfo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

class JWTGenerate(private val applicationInfo: ApplicationInfo) {

    fun tokenGenerate(claims: Map<String, Any>, subject: String): String = Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + applicationInfo.jwtExpirationTime))
        .signWith(SignatureAlgorithm.HS512, applicationInfo.securitySecret)
        .setHeaderParams(mapOf("typ" to "JWT"))
        .compact()
}
