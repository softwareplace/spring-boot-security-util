import com.gradle.kts.build.source.jsonWebToken
import com.gradle.kts.build.source.passay
import com.gradle.kts.build.source.springSecurity
import com.gradle.kts.build.source.test

plugins {
    val kotlinVersion = "1.7.22"
    `maven-publish`
    kotlin("jvm") version kotlinVersion
    id("build-source-plugin")
    id("build-source-application-plugin")
}

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven("https://jitpack.io")
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(11))
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val currentVersion = "0.0.6"
group = "com.softwareplace"
version = currentVersion

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-util"
            version = currentVersion

            from(components["java"])
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["java"])
                groupId = "com.github.eliasmeireles"
                artifactId = "spring-boot-security-util"
                version = currentVersion
            }
        }
    }
}


dependencies {
    springSecurity()
    jsonWebToken()
    passay()
    test()
}
