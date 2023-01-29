package com.softwareplace.authorization

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

abstract class CustomAuthenticationProcessingFilter protected constructor() :
    AbstractAuthenticationProcessingFilter(AntPathRequestMatcher("/authorization", "POST"))
