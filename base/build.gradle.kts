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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-util"
            version = "0.0.2"

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
                version = "0.0.1"
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
group = "com.softwareplace"
version = "1.0.0"
