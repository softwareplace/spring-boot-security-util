package com.softwareplace.springsecurity.example

import com.softwareplace.springsecurity.SpringSecurityInit
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.util.*

@SpringBootApplication
@ComponentScan(basePackageClasses = [SpringSecurityInit::class])
class MainApp

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"))
    SpringApplication.run(MainApp::class.java, *args)
}
