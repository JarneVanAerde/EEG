package be.kdg.eeg

import be.kdg.eeg.model.stimulus.StimulusService

object MainTest {
  def main(args: Array[String]): Unit = {
    val service1 = new StimulusService("files/Barbara_NounVerb.csv", "Barbara")
    val service2 = new StimulusService("files/Bart_NounVerb.csv", "Bart")
    val test1 = service1.analyseTools.getSlidingWindowAvgs("televisie", "AF3", 4)
    val test2 = service2.analyseTools.getSlidingWindowAvgs("televisie", "AF3", 4)
    print("done.")
  }
}
