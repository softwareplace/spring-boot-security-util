import java.util.*

rootProject.name = "spring-boot-security-util-example"

include(":app")
include(":security")


val properties = Properties()
val inputStream = rootDir.resolve("../spring-boot-builder-plugin/build-configuration/gradle.properties").inputStream()
properties.load(inputStream)

properties.forEach { (key, value) ->
    System.setProperty(key.toString(), value.toString())
}

project(":security").projectDir = file("../app")


includeBuild("../spring-boot-builder-plugin/build-configuration")
includeBuild("../spring-boot-builder-plugin/source/source-kotlin")
includeBuild("../spring-boot-builder-plugin/openapi/openapi-kotlin")


