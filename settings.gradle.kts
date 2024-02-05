rootProject.name = "spring-boot-security-util"

include(":app")

pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://repo.spring.io/milestone")
    }
}

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.github.softwareplace.springboot:plugins:0.0.1")
    }
}





