package be.kdg.eeg.models.services

import be.kdg.eeg.models.Stimulus
import be.kdg.eeg.models.utils.FileLoader

class StimulusService(val fileForStimulus: String) {
  val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  val stimuli: Vector[Stimulus] = makeStimuli()
  val header: Vector[String] = fileLoader.getheader

  def makeStimuli(): Vector[Stimulus] = {
    val offeredStrings: Vector[String] = fileLoader.getOfferdStrings
    val header: Vector[String] = fileLoader.getheader
    val codePointValues: Vector[Vector[String]] = fileLoader.getCodePointValues

    //make stimuli based on the offerd strings
    val stimuli = offeredStrings.map(
      offerString => new Stimulus(null, offerString, null))

    stimuli
  }

  def printStimuli(): Unit = {
    stimuli.foreach(
      stim => println(stim.offerdString)
    )
  }
}
