organization   := "com.spavikevik"
name           := "swagger-finatra"
scalaVersion   := "2.12.10"
version        := "0.5.0"

lazy val finatraVersion = "18.12.0"

scalacOptions ++= Seq(
  "-deprecation"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % finatraVersion,
  "io.swagger" % "swagger-core" % "1.5.23",
  "io.swagger" %% "swagger-scala-module" % "1.0.6",
  "org.webjars" % "swagger-ui" % "3.24.3",
  "net.bytebuddy" % "byte-buddy" % "1.10.5",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

lazy val root = Project("swagger-finatra", file("."))
