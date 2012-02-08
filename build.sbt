name := "wordnik_api_dsl"

version := "1.0"

scalaVersion := "2.9.1"

organization := "wordnik.com"

mainClass := Some("com.wordnik.api.dsl.ApiDsl")

logLevel in compile := Level.Warn

seq(assemblySettings: _*)

libraryDependencies ++= Seq(
		    "net.databinder" % "dispatch-http_2.9.1" % "0.8.7",
		    "net.liftweb" % "lift-json_2.9.1" % "2.4" 
)