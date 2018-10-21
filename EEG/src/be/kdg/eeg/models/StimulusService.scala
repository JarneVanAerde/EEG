package be.kdg.eeg.models

import be.kdg.eeg.models.utils.FileLoader

class StimulusService(val fileForStimulus: String) {
  val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  val stimuli: Vector[Stimulus] = makeStimuli()

  def makeStimuli(): Vector[Stimulus] = {
    val stimuli: Vector[Stimulus] = Vector()
    fileLoader.getOfferdStrings.foreach(
      stimuli :+ new Stimulus(null, _, null)
    )
    stimuli
  }

  def printStimuli(): Unit = {
    stimuli.foreach(
      stim => println(stim.getOfferdString)
    )
  }
}
