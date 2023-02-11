import com.gradle.kts.build.source.jsonWebToken
import com.gradle.kts.build.source.passay
import com.gradle.kts.build.source.springSecurity
import com.gradle.kts.build.source.test

plugins {
    `maven-publish`
    kotlin("jvm") version System.getProperty("kotlinVersion")
    id("build-source-plugin")
    id("build-submodule-source-plugin")
}

val currentVersion = "0.0.13-SNAPSHOT"
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
