import com.gradle.kts.build.source.jsonWebToken
import com.gradle.kts.build.source.passay
import com.gradle.kts.build.source.springSecurity
import com.gradle.kts.build.source.test

plugins {
    `maven-publish`
    `kotlin-dsl`
    id("build-source-plugin")
    id("build-source-application-plugin")
    id("build-source-project-plugin")
}

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven("https://jitpack.io")
}

val currentVersion = "0.0.2"
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
