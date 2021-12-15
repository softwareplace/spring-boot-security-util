package com.softwareplace.authorization

import com.softwareplace.service.AuthorizationUserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

class JWTGenerate(private val authorizationUserService: AuthorizationUserService) {

    fun tokenGenerate(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + authorizationUserService.expirationTime()))
            .signWith(SignatureAlgorithm.HS512, authorizationUserService.authorizationSecrete())
            .setHeaderParams(mapOf("typ" to "JWT"))
            .compact()
    }
}
