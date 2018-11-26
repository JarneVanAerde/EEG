package be.kdg.eeg

import be.kdg.eeg.model.stimulus.StimulusService

object MainTest {
  def main(args: Array[String]): Unit = {
    new StimulusService("files/Barbara_NounVerb.csv").stimuli
  }
}
