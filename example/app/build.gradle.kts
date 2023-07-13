import com.gradle.kts.kotlin.buildsource.*
import com.gradle.kts.kotlin.openapi.OpenApiSettings
import com.gradle.kts.kotlin.openapi.openApiSettings

plugins {
    id("source-plugin")
    id("openapi-plugin")
}

group = "com.softwareplace.springsecurity.example"
version = "1.0.0"

openApiSettings(OpenApiSettings(reactive = true))

dependencies {
    implementation(project(":security"))
    springSecurity()
    springJettyApi()
    springWebFlux()
    jsonWebToken()
    jsonLogger()
    mappstruct()
    passay()
    test()
}
