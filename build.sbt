name := "Streaming"

version := "0.1"

scalaVersion := "2.11.8"


libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.0"
// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"
//libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.4.1"
// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"


