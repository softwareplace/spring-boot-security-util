import com.gradle.kts.kotlin.buildsource.*

plugins {
    id("submodule-source-plugin")
}

val currentVersion = "0.0.18"
val appGroup = "com.softwareplace.springsecurity"

group = appGroup
version = currentVersion

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = appGroup
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
    springConfigurationProcessor()
    springSecurity()
    jsonWebToken()
    jsonLogger()
    passay()
    test()
}
