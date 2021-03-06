organization       := "com.spavikevik"
name               := "swagger-finatra"
scalaVersion       := "2.12.11"
version            := "20.9.0"

lazy val finatraVersion = "20.9.0"

scalacOptions ++= Seq(
  "-deprecation"
)

resolvers += "Jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % finatraVersion,
  "com.twitter" %% "finatra-http-mustache" % finatraVersion,
  "io.swagger" % "swagger-core" % "1.6.2",
  "com.github.spavikevik" % "swagger-scala-module" % "v1.0.8",
  "org.webjars" % "swagger-ui" % "3.26.1",
  "net.bytebuddy" % "byte-buddy" % "1.10.5",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

lazy val root = Project("swagger-finatra", file("."))

val examplesLibs = Seq(
  "com.twitter" %% "finatra-http" % finatraVersion % "test" classifier "tests",
  "com.twitter" %% "inject-app" % finatraVersion % "test" classifier "tests",
  "com.twitter" %% "inject-core" % finatraVersion % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % finatraVersion % "test" classifier "tests",
  "com.twitter" %% "inject-server" % finatraVersion % "test" classifier "tests",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val example = Project("swagger-finatra-example", file("example"))
  .dependsOn(root)
  .settings(libraryDependencies ++= examplesLibs)