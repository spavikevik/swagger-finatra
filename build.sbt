organization       := "com.spavikevik"
name               := "swagger-finatra"
scalaVersion       := "2.12.11"
version            := "0.5.0"
crossScalaVersions := Seq("2.11.12", "2.12.11")

lazy val finatraVersion = "20.5.0"

scalacOptions ++= Seq(
  "-deprecation"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % finatraVersion,
  "com.twitter" %% "finatra-http-mustache" % finatraVersion,
  "io.swagger" % "swagger-core" % "1.5.24",
  "io.swagger" %% "swagger-scala-module" % "1.0.6",
  "org.webjars" % "swagger-ui" % "3.25.0",
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