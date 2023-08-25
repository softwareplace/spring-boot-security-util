rootProject.name = "spring-boot-security-util-example"

include(":app")
include(":security")

apply(from = "../spring-boot-builder-plugin/libs.settings.gradle.kts")

project(":security").projectDir = file("../app")


includeBuild("../spring-boot-builder-plugin/build-configuration")
includeBuild("../spring-boot-builder-plugin/source/source-kotlin")
includeBuild("../spring-boot-builder-plugin/openapi/openapi-kotlin")


