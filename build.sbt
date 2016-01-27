
name := "htmlToScalatags"

version := "1.0"

scalaVersion := "2.11.7"

enablePlugins(ScalaJSPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom"    % "0.8.0" withJavadoc() withSources(),
  "com.lihaoyi"  %%% "fastparse"      % "0.3.4" withJavadoc() withSources(),
  "com.lihaoyi"  %%% "scalatags"      % "0.5.2" withJavadoc() withSources()
//  "be.doeraene"  %%% "scalajs-jquery" % "0.8.1" withJavadoc() withSources()
)

mainClass in Compile := Some("com.scalway.OnlineParser")
mainClass in (Compile, run) := Some("com.scalway.ConsoleParser")

persistLauncher := true

