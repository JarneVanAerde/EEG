package be.kdg.eeg.models.utils

import scala.collection.mutable.ArrayBuffer

class FileLoader(val fileName: String) {
  val unParsedData: Array[Array[String]] = loadFile()

  def getOfferdStrings: Vector[String] = {
    unParsedData.filter(_ (0).toLowerCase.contains("stimulus"))
      .map(_ (1)).toVector
  }

  def getCodePointValues: Vector[Vector[String]] = {
    unParsedData.filter(!_ (0).toLowerCase.contains("stimulus")).tail
      .map(_.toVector).toVector
  }

  def getheader: Vector[String] = {
    unParsedData.head.toVector
  }

  def loadFile(): Array[Array[String]] = {
    //check if filename is empty
    if (fileName.isEmpty) {
      println("Filename is empty")
      return null
    }

    val rows = ArrayBuffer[Array[String]]()

    // read the csv data
    using(io.Source.fromFile(fileName)) { source =>
      source.getLines.foreach(
        line => rows += line.split("\t").map(_.trim)
      )
    }

    rows.toArray
  }

  def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
