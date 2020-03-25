package com.dtreaming.DStream

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Duration, Durations, Seconds, StreamingContext}


object ViewingFiguresStreaming extends App {

  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR)

  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val ssc = new StreamingContext(conf, Seconds(1))

  val topics = Array("viewrecords")

  val kafkaParams = Map[String, Object](
    //    "bootstrap.servers" -> "192.168.247.129:9092",
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "spark-group-1",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    LocationStrategies.PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  val result = stream.map(item => item.value())

  val mostWatched = result.map(item => (item, 5L))
    .reduceByKeyAndWindow((x:Long, y:Long) => x + y , Durations.minutes(60),Durations.seconds(20)).map(item => item.swap)
      .transform(x => x.sortByKey(false))



  mostWatched.print()

  ssc.start()

  ssc.awaitTermination()


}
