package be.kdg.eeg.model.shared

import scala.collection.mutable.ArrayBuffer

/**
  * This class is used for loading unparsed data into the program
  *
  * @param fileName The file it needs to extract the data from
  */
class FileLoader(val fileName: String) {
  val unParsedData: Array[Array[String]] = loadFile()

  /**
    * Loads all data into a 2d array of strings
    *
    * @return The unparsed data
    */
  private def loadFile(): Array[Array[String]] = {
    //check if filename is empty
    if (fileName.isEmpty) {
      println("Filename is empty")
      return null
    }

    val rows = ArrayBuffer[Array[String]]()

    // read the csv data
    using(io.Source.fromFile(fileName)) { source =>
      source.getLines.foreach(line =>
        rows += line.split("\t").map(_.trim)
      )
    }

    rows.toArray
  }

  private def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
