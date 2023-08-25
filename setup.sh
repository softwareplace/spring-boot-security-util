#!/bin/bash

rm -rf spring-boot-builder-plugin

git submodule update --recursive --init
git submodule update --recursive --remote
