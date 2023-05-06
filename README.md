# Lib Spring boot Security Util

## Requirements

- `Java 11` or higher

****

## Initialization

- Required beans

> [SpringSecurityInit](src/main/kotlin/com/softwareplace/springsecurity/SpringSecurityInit.kt)
>
> [AuthorizationHandler](src/main/kotlin/com/softwareplace/springsecurity/authorization/AuthorizationHandler.kt)
>
> [AuthorizationUserService](src/main/kotlin/com/softwareplace/springsecurity/service/AuthorizationUserService.kt)
>
> [ScopeService](src/main/kotlin/com/softwareplace/springsecurity/service/ScopeService.kt)

```kotlin
@SpringBootApplication
@ImportAutoConfiguration(classes = [SpringSecurityInit::class])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
```

- Controller Advisor

```kotlin
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ControllerAdvice(annotations = [RestController::class])
class ControllerAdviceImpl(mapper: ObjectMapper) : ControllerExceptionAdvice(mapper)
```

`application.yaml`

```yaml
spring:
  security-util:
    jwtExpirationTime: ${ENV_JWT_EXPIRATION_TIME}
    securitySecret: ${ENV_SECURITY_SECRET}
    openUrl:
      - "/swagger-resources/**"
      - "/swagger-ui.html/**"
      - "/swagger-config/**"
      - "/source-handler/**"
      - "/authorization"
      - "/swagger-ui/**"
      - "/favicon.ico/**"
      - "/v3/api-docs/**"
      - "/v3/api-docs"
      - "/webjars/**"
      - "/swagger/**"
      - "/v1/lights"
      - "/v1/listener"
      - "/health/**"
      - "/assets/**"
      - "/error/**"
      - "/csrf/**"
      - "/docs/**"
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
