package com.dtreaming.DStream

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.functions._

object ViewingFiguresStructuredStream extends App {
  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR)
  val session = SparkSession.builder()
    .master("local[*]")
    .appName("Structured-stream")
    .config("spark.sql.shuffle.partitions","10")
    .getOrCreate()

  val df = session.readStream.format("kafka")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("subscribe", "viewrecords")
    .option("kafak.value.deserializer", classOf[StringDeserializer].toString)
    .load()

  import session.implicits._

  val result = df.withColumn("viewtime", lit(5))
    .select($"value".cast(StringType), $"viewtime",$"timestamp")
    .groupBy($"value" , window($"timestamp", "2 minutes")).sum("viewtime")
    .orderBy("value")

  df.printSchema()
  val query = result.writeStream.format("console")
    .outputMode(OutputMode.Complete())
    .option("truncate",false)
    .option("numRows",50)
    .start()

  query.awaitTermination()


}
