package be.kdg.eeg.model.shared

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * This class is used for loading un-parsed data into the program
  *
  * @param fileName The file it needs to extract the data from
  */
final class FileLoader(val fileName: String) {
  /**
    * Loads all data into a 2d array of strings
    *
    * @return The un-parsed data
    */
  def loadFile: Array[Array[String]] = {
    //check if filename is empty
    if (fileName.isEmpty)
      throw new Exception("Filename is empty")

    val rows = ArrayBuffer[Array[String]]()

    // read the csv data
    using(io.Source.fromFile(fileName)) { source =>
      source.getLines.foreach(line =>
        rows += line.split("\t").map(_.trim)
      )
    }

    rows.toArray
  }

  /**
    * Function that uses a resource to load a file.
    */
  private def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  /**
    * This function can be used to load a simple txt file into memory.
    *
    * @param fileName The name of the file.
    * @return An array all the string elements.
    */
  def loadOrdinaryFile(fileName: String): Array[String] = {
    Source.fromFile(fileName).getLines.toArray
  }
}
