#!/bin/bash

mvn clean package

rm -Rf ~/.m2/repository/com/softwareplace/security/spring-boot-security-util/0.0.1/

mkdir -p ~/.m2/repository/com/softwareplace/security/spring-boot-security-util/0.0.1/

cp target/spring-boot-security-util-0.0.1.jar ~/.m2/repository/com/softwareplace/security/spring-boot-security-util/0.0.1/
cp pom.xml ~/.m2/repository/com/softwareplace/security/spring-boot-security-util/0.0.1/spring-boot-security-util-0.0.1.pom
