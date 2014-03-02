name := "BrushUpIdeas"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.twitter4j" % "twitter4j-core" % "3.0.5"
)

play.Project.playScalaSettings
