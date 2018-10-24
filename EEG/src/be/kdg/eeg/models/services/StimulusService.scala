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

    //make stimuli based on the offerd strings
    val stimuli: List[Stimulus] = List()
    for (i <- 0 to offeredStrings.size) {
      val stimulus = new Stimulus(null, offeredStrings(0), null)
      stimuli.add
    }

    stimuli
  }

  def getContactPoints(unparsedVector: Vector[Vector[String]]): Vector[ContactPointValue] = {

    for (head <- 0 to )
    for (i <- 0 to unparsedVector.size) {
      for (value <- 0 to unparsedVector(i).size) {

      }
    }
    val headeredContactPoints = header.map(

      new ContactPointValue(_, null, null
    ))

    headeredContactPoints.
  }

  def printStimuli(): Unit = {
    stimuli.foreach(
      stim => println(stim.offerdString)
    )
  }
}
