package be.kdg.eeg.models.utils

import scala.collection.mutable.ArrayBuffer

class FileLoader(val fileName: String) {
  val unParsedData: Array[Array[String]] = loadFile()

  def loadFile(): Array[Array[String]] = {
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

  def using[A <: {def close() : Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  /*
  def getCodePointValues: Vector[Vector[String]] = {
    unParsedData.tail
      .map(_.slice(3, 17))
      .map(_.toVector).toVector
      .partition(_.contains("simulus"))

    null
  }

  def getheader: Vector[String] = {
    unParsedData.head.slice(3, 17).toVector
  }
  */
}

/*
implicit class TakeUntilListWrapper[T](list: List[T]) {
  def takeUntil(predicate: T => Boolean):List[T] = {
    list.span(predicate) match {
      case (head, tail) => head ::: tail.take(1)
    }
  }
}
*/
