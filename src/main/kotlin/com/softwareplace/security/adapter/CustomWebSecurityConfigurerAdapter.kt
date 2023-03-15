package com.softwareplace.security.adapter


import com.softwareplace.authorization.AuthorizationHandler
import com.softwareplace.config.ApplicationInfo
import com.softwareplace.config.ControllerExceptionAdvice
import com.softwareplace.encrypt.Encrypt
import com.softwareplace.model.UserData
import com.softwareplace.security.filter.JWTAuthenticationFilter
import com.softwareplace.security.filter.JWTAuthorizationFilter
import com.softwareplace.service.AuthorizationUserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

inline fun <reified T : UserData> getSessionUser(): T? {
    val servletRequest: HttpServletRequest = (RequestContextHolder.currentRequestAttributes()
            as ServletRequestAttributes).request
    return servletRequest.getAttribute(JWTAuthorizationFilter.USER_SESSION_DATA) as T?
}

@Configuration
@EnableWebSecurity
class CustomWebSecurityConfigurerAdapter(
    private val userDetailsService: UserDetailsService,
    private val authorizationUserService: AuthorizationUserService,
    private val authorizationHandler: AuthorizationHandler,
    private val applicationInfo: ApplicationInfo,
    private val controllerAdvice: ControllerExceptionAdvice,
) : WebSecurityConfigurerAdapter() {

    private val authenticationFilter: JWTAuthenticationFilter by lazy {
        JWTAuthenticationFilter(
            authorizationUserService,
            authenticationManager(),
            authorizationHandler,
            applicationInfo
        )
    }

    private val jwtAuthorizationFilter: JWTAuthorizationFilter by lazy {
        JWTAuthorizationFilter(authorizationUserService, authorizationHandler, applicationInfo)
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers(*applicationInfo.openUrl.toTypedArray()).permitAll()

        authorizationHandler.userConfig().forEach {
            http.authorizeRequests()
                .antMatchers(it.first)
                .hasAnyRole(*it.second.toTypedArray())
        }

        http.exceptionHandling()
            .authenticationEntryPoint(controllerAdvice)
            .accessDeniedHandler(controllerAdvice)
            .and()
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic().and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(Encrypt.PASSWORD_ENCODER)
    }
}
