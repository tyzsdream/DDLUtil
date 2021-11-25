mvn clean deploy -P sonatype-oss-release -Darguments="gpg.passphrase=1qaz2wsX$321"

mvn clean deploy -Dmaven.test.skip=true -Dmaven.javadoc.skip=true