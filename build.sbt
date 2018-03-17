name := "imgqueue"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "9.4.8.v20171121"

libraryDependencies += "org.eclipse.jetty.websocket" % "javax-websocket-server-impl" % "9.4.8.v20171121"

libraryDependencies += "javax.websocket" % "javax.websocket-api" % "1.0"

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

mainClass in assembly := Some("imgqueue.Starter")

assemblyJarName in assembly := "imgqueue.jar"

