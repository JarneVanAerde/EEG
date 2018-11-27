package be.kdg.eeg

import be.kdg.eeg.model.stimulus.StimulusService

object MainTest {
  def main(args: Array[String]): Unit = {
    val service = new StimulusService("files/Barbara_NounVerb.csv")
    val test = service.getContactPointValuesForStimulus("televisie", "AF3")
    print("test")
  }
}
