package be.kdg.eeg

import be.kdg.eeg.models.services.StimulusService

object MainTest {
  def main(args: Array[String]): Unit = {
    val stimSer = new StimulusService("files/Barbara_NounVerb.csv")
    stimSer.printStimuli()
  }
}
