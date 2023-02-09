# Lib Spring boot Security Util

## Requirements

- `Java 11` or higher

****

## Initialization

- Required beans

> [SpringSecurityInit](src/main/kotlin/com/softwareplace/config/SpringSecurityInit.kt)
>
> [ControllerExceptionAdvice](src/main/kotlin/com/softwareplace/config/ControllerExceptionAdvice.kt)
>
> [AuthorizationHandler](src/main/kotlin/com/softwareplace/authorization/AuthorizationHandler.kt)
>
> [AuthorizationUserService](src/main/kotlin/com/softwareplace/service/AuthorizationUserService.kt)
>
> [UserDetailsService](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetailsService.html)

```kotlin
@SpringBootApplication
@EnableConfigurationProperties(value = [ApplicationInfo::class])
class App : SpringSecurityInit

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
```

`application.yaml`

```yaml
spring:
  security-util:
    jwtExpirationTime: ${ENV_SP_JWT_EXPIRATION_TIME}
    securitySecret: ${ENV_SECURITY_SECRET}
    allowedOrigins:
      - http://localhost:8080
    openUrl:
      - swagger-ui.html
      - swagger-config
      - authorization
      - swagger-ui
      - v3/api-docs
      - favicon.ico
      - webjars
      - swagger
```

### Dependency setting

- Gradle

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

```kotlin
implementation("com.github.eliasmeireles:spring-boot-security-util:$spring-boot-security-util-version")
```

- Maven

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml

<dependency>
    <groupId>com.github.eliasmeireles</groupId>
    <artifactId>spring-boot-security-util</artifactId>
    <version>${spring-boot-security-util-version}</version>
</dependency>
```
