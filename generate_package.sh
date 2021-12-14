#!/bin/bash

mvn clean package

mvn install:install-file -Dfile=target/spring-boot-security-util-"$1".jar \
 -DgroupId=com.softwareplace -DartifactId=spring-boot-security-util -Dversion="$1" -Dpackaging=jar

