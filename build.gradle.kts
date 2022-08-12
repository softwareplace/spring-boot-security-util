plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

repositories {
    mavenCentral()
}

group = "br.com.softwareplace"
version = "1.0.0"

dependencies {
    val springBootVersion = "2.6.2"
    implementation(project(":json-logger"))
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.passay:passay:1.6.1")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    implementation("io.jsonwebtoken:jjwt:0.9.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}
