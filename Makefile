update:
	git submodule update --recursive --init
	git submodule update --recursive --remote

local-publish:
	./gradlew publishToMavenLocal

test:
	./gradlew test
