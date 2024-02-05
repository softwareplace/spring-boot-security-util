import com.github.softwareplace.springboot.kotlin.*

plugins {
    id("com.github.softwareplace.springboot.kotlin")
    id("com.github.softwareplace.springboot.kotlin-openapi")
}

group = "com.softwareplace.springsecurity.example"
version = "1.0.0"

openApiSettings(OpenApiSettings(reactive = true))

dependencies {
    implementation(project(":security"))
    springSecurity()
    springJettyApi()
    springWebFlux()
    springDataJpa()
    jsonWebToken()
    jsonLogger()
    postgresql()
    mappstruct()
    retrofit2()
    passay()

    testContainersPostgresql()
    test()
}
