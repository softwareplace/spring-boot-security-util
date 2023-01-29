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
            artifactId = "spring-boot-security-utils"
            version = "1.0.0"

            from(components["java"])
        }
    }
}

group = "com.softwareplace"
version = "1.0.0"
