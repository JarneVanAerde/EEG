package be.kdg.eeg.models.services

import be.kdg.eeg.models.Stimulus
import be.kdg.eeg.models.utils.FileLoader

class StimulusService(val fileForStimulus: String) {
  val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  val stimuli: Vector[Stimulus] = makeStimuli()

  def makeStimuli(): Vector[Stimulus] = {
    fileLoader.getOfferdStrings.map(
      offerString => new Stimulus(null, offerString, null)
    )
  }

  def printStimuli(): Unit = {
    stimuli.foreach(
      stim => println(stim.getOfferdString)
    )
  }
}
