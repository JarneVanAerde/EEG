package be.kdg.eeg.model.shared

import be.kdg.eeg.model.contactPoint.ContactPointValue
import be.kdg.eeg.model.stimulus.Stimulus

class DataBinder(val fileForStimulus: String) {
  val unParsedData: Array[Array[String]] = new FileLoader(fileForStimulus).unParsedData

  def getParsedData: Vector[Stimulus] = {
    val zip = unParsedData.zipWithIndex.filter(x => x._1.length <= 2)
    val parsedData = zip.map(stim => new Stimulus(null, stim._1(1), getCodePointsForStimulus(Vector[Vector[ContactPointValue]](), stim._2 + 1))).toVector
    parsedData
  }

  def getCodePointsForStimulus(values: Vector[Vector[ContactPointValue]], counter: Int): Vector[Vector[ContactPointValue]] = {
    if (!unParsedData(counter)(0).contains("stimulus") || counter != unParsedData.length - 1) {
      val new_values = values :+ getCodePointsForStimulusRow(unParsedData(counter))
      getCodePointsForStimulus(new_values, counter + 1)
    } else values
  }

  def getCodePointsForStimulusRow(row: Array[String]): Vector[ContactPointValue] = {
    row.map(point => new ContactPointValue(null, point.toDouble, null)).toVector
  }
}
