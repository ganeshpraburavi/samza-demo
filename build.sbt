val samzaVersion = "0.9.1"
val samzaCore = "org.apache.samza" % "samza-core_2.10" % samzaVersion
val samzaYarn = "org.apache.samza" % "samza-yarn_2.10" % samzaVersion
val samzaKafka = "org.apache.samza" % "samza-kafka_2.10" % samzaVersion
val samzaLog4j = "org.apache.samza" % "samza-log4j" % samzaVersion
val slf4jLog4j12 = "org.slf4j" % "slf4j-log4j12" % "1.6.2" % "runtime"
val kafka = "org.apache.kafka" % "kafka_2.10" % "0.8.2.1"

val appVersion = "1.0"

lazy val root =
  Project(
    id = "samza-demo",
    base = file("."),
    settings = packAutoSettings ++
      Seq(
        organization := "samza-demo",
        version := appVersion,
        scalaVersion := "2.10.4",
        unmanagedBase := file(".") / "lib",
        organizationName := ""
      ) ++
      net.virtualvoid.sbt.graph.Plugin.graphSettings ++
      Seq(
        libraryDependencies ++= Seq(samzaCore, samzaYarn, samzaKafka, kafka, samzaLog4j, slf4jLog4j12),
        publish := {},
        packResourceDir ++= Map(
          baseDirectory.value / "bin" -> "bin",
          baseDirectory.value / "src/main/resources" -> "config",
          baseDirectory.value / "src/main/resources/badwords.txt" -> "lib/badwords.txt",
          baseDirectory.value / "src/main/resources/simplestreamtask.properties" -> "lib/simplestreamtask.properties",
          baseDirectory.value / "src/main/resources/log4j.xml" -> "lib/log4j.xml"),
        packDir := "samza-demo")
  )
