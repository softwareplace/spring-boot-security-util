init:
	./init

local-publish:
	./init
	./gradlew publishToMavenLocal

test:
	./gradlew test
