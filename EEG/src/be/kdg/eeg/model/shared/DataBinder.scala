package be.kdg.eeg.model.shared

import be.kdg.eeg.model.contactPoint.ContactPointValue
import be.kdg.eeg.model.stimulus.Stimulus

class DataBinder(val fileForStimulus: String) {
  val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  val unParsedData: Array[Array[String]] = fileLoader.unParsedData
  val header: Array[String] = fileLoader.unParsedData(0)

  def getParsedData: Vector[Stimulus] = {
    val zip = unParsedData.zipWithIndex.filter(x => x._1.length <= 2)
    val parsedData = zip.map(stim => new Stimulus(null, stim._1(1), getCodePointsForStimulus(stim._2 + 1))).toVector
    parsedData
  }

  private def getCodePointsForStimulus(counter: Int, values: Vector[Vector[ContactPointValue]] = Vector[Vector[ContactPointValue]]()): Vector[Vector[ContactPointValue]] = {
    if (counter < unParsedData.length && !unParsedData(counter)(0).contains("stimulus")) {
      val new_values = values :+ getCodePointsForStimulusRow(unParsedData(counter))
      getCodePointsForStimulus(counter + 1, new_values)
    } else values
  }

  private def getCodePointsForStimulusRow(row: Array[String], row_values: Vector[ContactPointValue] = Vector[ContactPointValue](), counter: Int = 3): Vector[ContactPointValue] = {
    if (counter <= 16) {
      val new_row_values = row_values :+ new ContactPointValue(header(counter), row(counter).toDouble, null)
      getCodePointsForStimulusRow(row, new_row_values, counter + 1)
    } else row_values
  }
}
