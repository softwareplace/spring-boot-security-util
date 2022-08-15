import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "br.com.softwareplace"
            artifactId = "spring-boot-security-utils"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

group = "br.com.softwareplace"
version = "1.0.0"

dependencies {
    val springBootVersion = "2.7.2"
    implementation("br.com.softwareplace:json-logger:1.0.0")

    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")

    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.passay:passay:1.6.1")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    implementation("io.jsonwebtoken:jjwt:0.9.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}

tasks.withType<KotlinCompile> {

}

tasks.withType<Test> {
    description = "Runs unit tests"
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStackTraces = true
        exceptionFormat = FULL
        events = mutableSetOf(
                FAILED,
                SKIPPED
        )
    }
}