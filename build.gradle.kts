import com.gradle.kts.build.source.fasterXmlJackson
import com.gradle.kts.build.source.jsonLogger
import com.gradle.kts.build.source.springSecurity

plugins {
    `maven-publish`
    `kotlin-dsl`
    kotlin("jvm") version "1.7.22"
    id("build-source-application-plugin")
    id("build-source-project-plugin")
    id("build-source-plugin")
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-util"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

dependencies {
    springSecurity()
    fasterXmlJackson()
    jsonLogger()
}

group = "com.softwareplace"
version = "1.0.0"
