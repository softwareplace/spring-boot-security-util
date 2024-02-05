import java.util.*

rootProject.name = "spring-boot-security-util-example"

include(":app")
include(":security")




project(":security").projectDir = file("../app")

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
        maven("https://repo.spring.io/milestone")
    }
    dependencies {
        classpath("com.github.softwareplace.springboot:plugins:0.0.1-SNAPSHOT")
    }
}
