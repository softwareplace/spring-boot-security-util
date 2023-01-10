plugins {
    `maven-publish`
    `kotlin-dsl`
    kotlin("jvm") version "1.7.22"
    id("build-source-plugin")
    id("build-source-project-plugin")
    id("build-source-application-plugin")
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-utils"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

group = "com.softwareplace"
version = "1.0.0"
