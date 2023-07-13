import com.gradle.kts.kotlin.buildsource.*

plugins {
    `maven-publish`
    id("submodule-source-plugin")
}

val currentVersion = "1.0.1-SNAPSHOT"
val appGroup = "com.softwareplace.springsecurity"

group = appGroup
version = currentVersion

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.eliasmeireles"
            artifactId = "spring-boot-security-util"
            version = currentVersion
            java.sourceCompatibility = JavaVersion.VERSION_19
            java.targetCompatibility = JavaVersion.VERSION_19

            from(components["java"])
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.eliasmeireles"
            artifactId = "spring-boot-security-util"
            version = currentVersion
            java.sourceCompatibility = JavaVersion.VERSION_19
            java.targetCompatibility = JavaVersion.VERSION_19

            from(components["java"])
        }
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
