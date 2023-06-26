#!/bin/bash

rm -rf spring-boot-included-builds

git submodule update --recursive --init
git submodule update --recursive --remote
