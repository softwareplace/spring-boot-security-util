# Lib Spring boot Security Util

## Requirements

- `Java 11` or higher

****

## Initialization

- Required beans

> [SpringSecurityUtilInitializer](security/src/main/kotlin/com/softwareplace/config/SpringSecurityUtilInitializer.kt)
>
> [ControllerExceptionAdvice](security/src/main/kotlin/com/softwareplace/config/ControllerExceptionAdvice.kt)
>
> [AuthorizationHandler](security/src/main/kotlin/com/softwareplace/authorization/AuthorizationHandler.kt)
>
> [AuthorizationUserService](security/src/main/kotlin/com/softwareplace/service/AuthorizationUserService.kt)
>
> [UserDetailsService](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetailsService.html)

```kotlin
@SpringBootApplication
class App : SpringSecurityUtilInitializer

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
