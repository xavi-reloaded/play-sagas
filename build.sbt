name := "feed-processor"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += ws
libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"
libraryDependencies += "com.voodoodyne.jackson.jsog" % "jackson-jsog" % "1.1.1"
libraryDependencies += "com.auth0" % "java-jwt" % "3.3.0"
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.20"
libraryDependencies += "com.lambdaworks" % "scrypt" % "1.4.0"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.263"
libraryDependencies += "net.coobird" % "thumbnailator" % "0.4.8"
libraryDependencies += "commons-io" % "commons-io" % "2.6"
libraryDependencies += "org.reflections" % "reflections" % "0.9.11"
libraryDependencies += "javax.mail" % "javax.mail-api" % "1.6.0"
libraryDependencies += "com.kstruct" % "gethostname4j" % "0.0.2"
libraryDependencies += "org.json" % "json" % "20180130"

libraryDependencies += "redis.clients" % "jedis" % "2.9.0"


libraryDependencies += "org.mockito" % "mockito-core" % "2.13.0"
libraryDependencies += "com.tngtech.java" % "junit-dataprovider" % "1.13.1" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test



testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Tests")))
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

