import com.github.softwareplace.springboot.kotlin.*

plugins {
    id("maven-publish")
    id("com.github.softwareplace.springboot.kotlin-submodule")
}

val currentVersion = "1.0.0"
val appGroup = "com.softwareplace.springsecurity"

val jdkVersion: String by project

group = appGroup
version = currentVersion

println(jdkVersion)

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.softwareplace"
            artifactId = "spring-boot-security-util"
            version = currentVersion
            java.sourceCompatibility = JavaVersion.toVersion(jdkVersion)
            java.targetCompatibility = JavaVersion.toVersion(jdkVersion)

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
