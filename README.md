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
security-util:
  name: project.name
  version: project.version
  openUrl: example -> health,docs
  jwtExpirationTime: (long) expiration
  securitySecret: secret key
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
