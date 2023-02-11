update:
	git submodule update --recursive --init
	git submodule update --recursive --remote

local-publish:
	./gradlew clean -Pgroup=com.github.eliasmeireles -xtest assemble publishToMavenLocal

test:
	./gradlew test
