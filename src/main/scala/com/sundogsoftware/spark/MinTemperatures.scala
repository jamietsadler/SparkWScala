package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.log4j._
import scala.math.min

/** Find the minimum temperature by weather station */
object MinTemperatures {


  def parseLine(line: String): (String, String, Float) = {
    val fieldsToSplit = line.split( ",")
    val station = fieldsToSplit(0)
    val entry = fieldsToSplit(2)
    val temp = fieldsToSplit(3).toFloat * 0.1f * (9.0f / 5.0f) * 32.0f
    return (station, entry, temp)

  }
    /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MinTemps")
    // Read each line of input data
    val lines = sc.textFile("data/1800.csv")
    
    // Convert to (stationID, entryType, temperature) tuples
    val parsedLines = lines.map(parseLine)
    
    // Filter out all but TMIN entries
    val minTemps = parsedLines.filter(x => x._2 == "TMIN")
    
    // Convert to (stationID, temperature)
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))
    
    // Reduce by stationID retaining the minimum temperature found

    val minTempsPerStation = stationTemps.reduceByKey( (x, y) => min(x, y))
    
    // Collect, format, and print the results
    val results = minTempsPerStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val temp = result._2
       val formattedTemp = f"$temp%.2f F"
       println(s"$station minimum temperature: $formattedTemp") 
    }
      
  }
}