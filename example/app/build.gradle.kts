import com.github.softwareplace.springboot.buildconfiguration.addSpringframeworkBoot
import com.github.softwareplace.springboot.kotlin.kotlinMapStruct
import com.github.softwareplace.springboot.kotlin.kotlinReactive
import com.github.softwareplace.springboot.kotlin.openapi.kotlinOpenApiSettings
import com.github.softwareplace.springboot.kotlin.testKotlinMockito
import com.github.softwareplace.springboot.utils.*

plugins {
    id("com.github.softwareplace.springboot.kotlin")
}

group = "com.softwareplace.springsecurity.example"
version = "1.0.0"

kotlinOpenApiSettings {

}

dependencies {
    addSpringframeworkBoot("spring-boot-starter-oauth2-resource-server")
    implementation(project(":security"))
    springBootStartWeb()
    kotlinMapStruct()
    kotlinReactive()
    springSecurity()
    springJettyApi()
    springWebFlux()
    springDataJpa()
    jsonLogger()
    postgresql()
    retrofit2()
    passay()

    testContainersPostgresql()
    testKotlinMockito()
}
