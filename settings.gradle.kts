rootProject.name = "spring-boot-security-util"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

apply(from = "spring-boot-included-builds/libs.settings.gradle.kts")
apply(from = "spring-boot-included-builds/kotlin-included.build.settings.gradle.kts")


