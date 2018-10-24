package be.kdg.eeg.models.services

import be.kdg.eeg.models.{ContactPointValue, Stimulus}
import be.kdg.eeg.models.utils.FileLoader

class StimulusService(val fileForStimulus: String) {
  val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  val stimuli: Vector[Stimulus] = makeStimuli()
  val header: Vector[String] = fileLoader.getheader

  private def makeStimuli(): Vector[Stimulus] = {
    val offeredStrings: Vector[String] = fileLoader.getOfferdStrings
    val header: Vector[String] = fileLoader.getheader
    val codePointValues: Vector[Vector[String]] = fileLoader.getCodePointValues

    offeredStrings.map(
      new Stimulus(null, _, getContactPointsForLine())
    )
  }

  private def getContactPoints(unparsedVector: Vector[Vector[String]]): Vector[Vector[ContactPointValue]] = {
    unparsedVector.map(
      codePoints => getContactPointsForLine(codePoints)
    )
  }

  private def getContactPointsForLine(unparsedVector: Vector[String]): Vector[ContactPointValue] = {
    val tempCp = unparsedVector.map(
     new ContactPointValue(null, _, null)
    )
  }

  def printStimuli(): Unit = {
    stimuli.foreach(
      stim => println(stim.word)
    )
  }
}
