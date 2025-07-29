default:
	lein uberjar

test::
	lein tests

install::
	cp target/uberjar/blog-generator-0.1-SNAPSHOT-standalone.jar /usr/local/bin/blogg.jar
