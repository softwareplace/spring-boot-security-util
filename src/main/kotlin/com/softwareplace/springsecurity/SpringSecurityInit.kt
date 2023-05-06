package com.softwareplace.springsecurity

import com.softwareplace.springsecurity.config.ApplicationInfo
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [ApplicationInfo::class])
@ComponentScan(basePackages = ["com.softwareplace.springsecurity"])
class SpringSecurityInit
