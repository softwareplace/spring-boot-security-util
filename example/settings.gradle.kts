rootProject.name = "spring-boot-security-util-example"

include(":app")
include(":security")

apply(from = "../spring-boot-included-builds/libs.settings.gradle.kts")

project(":security").projectDir = file("../app")


includeBuild("../spring-boot-included-builds/build-configuration")
includeBuild("../spring-boot-included-builds/source/source-kotlin")
includeBuild("../spring-boot-included-builds/openapi/openapi-kotlin")


