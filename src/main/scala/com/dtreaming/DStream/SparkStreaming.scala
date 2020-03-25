package com.dtreaming.DStream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}

object SparkStreaming extends App {

  Logger.getLogger("org.apache").setLevel(Level.WARN)
  Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR)

  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val ssc = new StreamingContext(conf, Seconds(10))


  val inputData = ssc.socketTextStream("localhost", 8989)

  val result = inputData.map(item => item)

  val transform = result.map(data => data.split(",")(0))
    .map(x => (x, 1L))


//  val newResult = transform.reduceByKey((x, y) => x + y)

  val windowVersion = transform.reduceByKeyAndWindow((x:Long, y:Long) => x + y,Durations.minutes(1),Durations.minutes(1))


//  newResult.print()


  windowVersion.print()
  // println(result.map(data => data.split(",")(0)))


  ssc.start()

  ssc.awaitTermination()

}
