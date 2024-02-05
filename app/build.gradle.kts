import com.github.softwareplace.springboot.buildconfiguration.Dependencies
import com.github.softwareplace.springboot.kotlin.*

plugins {
    id("maven-publish")
    id("com.github.softwareplace.springboot.kotlin-submodule")
}

val currentVersion = "0.0.4"
val appGroup = "com.softwareplace.springsecurity"

group = appGroup
version = currentVersion

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-util"
            version = currentVersion
            java.sourceCompatibility = JavaVersion.toVersion(Dependencies.Version.jdkVersion)
            java.targetCompatibility = JavaVersion.toVersion(Dependencies.Version.jdkVersion)

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
