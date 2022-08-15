init:
	./init

local-publish:
	./init
	./gradlew publishToMavenLocal

test:
	./init
	./gradlew test