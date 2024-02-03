local-publish:
	./gradlew clean -Pgroup=com.github.eliasmeireles -xtest assemble publishToMavenLocal --refresh-dependencies

test:
	./gradlew clean test
	make example-test

example-test:
	cd example && ./gradlew app:test --info
