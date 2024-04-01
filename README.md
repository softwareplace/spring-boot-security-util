# Spring boot Security Util

[![](https://jitpack.io/v/softwareplace/spring-boot-security-util.svg)](https://jitpack.io/#softwareplace/spring-boot-security-util)

## Requirements

- `Java 21`

****

## Initialization - Required beans

- [SpringSecurityInit](src/main/kotlin/com/softwareplace/springsecurity/SpringSecurityInit.kt) bean mapping
  initialization

- [AuthorizationHandler](src/main/kotlin/com/softwareplace/springsecurity/authorization/AuthorizationHandler.kt)
  whenever a request was successful or not, you will be notified.

- [AuthorizationUserService](src/main/kotlin/com/softwareplace/springsecurity/service/AuthorizationUserService.kt)
  responsible for locating the data of the user making the request

```kotlin
@SpringBootApplication
@ImportAutoConfiguration(classes = [SpringSecurityInit::class])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
```

## Generating simple api jwt generator key.

```shell
openssl genpkey -algorithm RSA -out ./private.key 
openssl rsa -pubout -in private.key -out ./pub.key   
```

`application.yaml`

```yaml
spring:
  security:
    encrypt-strength: 6
    pub-key: file:/opt/my-app/pub.key
    private-key: file:/opt/my-app/private.key
    jwtExpirationTime: ${ENV_JWT_EXPIRATION_TIME}
    securitySecret: ${ENV_SECURITY_SECRET}
    stackTraceLogEnable: false
    openUrl:
      - "/swagger-resources/**"
      - "/swagger-ui.html/**"
      - "/swagger-config/**"
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
implementation("com.github.softwareplace:spring-boot-security-util:$springBootSecurityUtilVersion")
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
    <groupId>com.github.softwareplace</groupId>
    <artifactId>spring-boot-security-util</artifactId>
    <version>${spring-boot-security-util-version}</version>
</dependency>
```
