package be.kdg.eeg

import java.util

import be.kdg.eeg.model.stimulus.StimulusServiceStore


object MainTest {
  def main(args: Array[String]): Unit = {
    val store: StimulusServiceStore = new StimulusServiceStore()
    val test = store.getService("Barbara").analyseTools.getInterestingData("televisie", "AF3", useAvg = true)
    print(test(0)._2)
  }
}
