#!/bin/bash

curl https://download.java.net/java/GA/jdk19.0.1/afdd2e245b014143b62ccb916125e3ce/10/GPL/openjdk-19.0.1_linux-x64_bin.tar.gz -o jdk19.tar.gz

tar -xzf jdk19.tar.gz

sdk install java 19 jdk-19.0.1/
sdk use java 19

java --version

rm -rf spring-boot-builder-plugin

git submodule update --recursive --init
git submodule update --recursive --remote
