target ?= 8.5
tag ?= 1.0.0-SNAPSHOT

publish-version:
	./gradlew clean -Pgroup=com.github.eliasmeireles -xtest assemble publishToMavenLocal --refresh-dependencies -Pversion=$(tag)

local-publish:
	./gradlew clean -Pgroup=com.github.eliasmeireles -xtest assemble publishToMavenLocal --refresh-dependencies

test:
	./gradlew clean test
	make example-test

example-test:
	cd example && ./gradlew app:test
