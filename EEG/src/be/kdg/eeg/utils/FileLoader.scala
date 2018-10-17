package be.kdg.eeg.utils

import scala.collection.mutable.ArrayBuffer

class FileLoader(val fileName: String) {
  def loadFile(): Unit = {
    // (0) check if filename is empty
    if (fileName.isEmpty) {
      println("Filename is empty")
      return
    }

    val rows = ArrayBuffer[Array[String]]()

    // (1) read the csv data
    using(io.Source.fromFile(fileName)) { source =>
      for (line <- source.getLines) {
        rows += line.split("\t").map(_.trim)
      }
    }

    // (2) print the results
    rows.foreach(
      row => println(s"${row(0)}|${row(1)}")
    )
  }

  def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
