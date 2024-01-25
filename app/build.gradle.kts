import com.gradle.kts.kotlin.buildsource.*

plugins {
    id("maven-publish")
    id("submodule-source-plugin")
}

val currentVersion = "v0.0.4"
val appGroup = "com.softwareplace.springsecurity"

group = appGroup
version = currentVersion

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))
    targetCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(System.getProperty("jdkVersion")))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.eliasmeireles"
            artifactId = "spring-boot-security-util"
            version = currentVersion
            java.sourceCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))
            java.targetCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))

            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}


dependencies {
    springConfigurationProcessor()
    springSecurity()
    jsonWebToken()
    jsonLogger()
    passay()
    test()
}
