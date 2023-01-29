rootProject.name = "spring-boot-security-util"
include(":base")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

includeBuild("spring-boot-included-builds/build-configuration")
includeBuild("spring-boot-included-builds/build-source")
