update:
	git submodule update --recursive --init
	git submodule update --recursive --remote

local-publish:
	make update
	./gradlew clean -Pgroup=com.github.eliasmeireles -xtest assemble publishToMavenLocal --refresh-dependencies

test:
	./gradlew clean test
	make example-test

example-test:
	cd example && ./gradlew app:test && cd ..
