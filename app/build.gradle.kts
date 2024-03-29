import com.github.softwareplace.springboot.kotlin.testKotlinMockito
import com.github.softwareplace.springboot.utils.*

plugins {
    id("maven-publish")
    id("com.github.softwareplace.springboot.kotlin")
}

val currentVersion = "1.0.0"
val appGroup = "com.softwareplace.springsecurity"

val jdkVersion: String by project

group = appGroup
version = currentVersion

submoduleConfig()

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
    springBootStartWeb()
    springSecurity()
    jsonWebToken()
    jsonLogger()
    passay()

    testKotlinMockito()
}
