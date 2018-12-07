package be.kdg.eeg

import be.kdg.eeg.model.stimulus.StimulusServiceStore


object MainTest {
  def main(args: Array[String]): Unit = {
    val store: StimulusServiceStore = new StimulusServiceStore()
    val test = store.getService("Barbara").analyseTools.getInterestingData("beloof", "AF3")
    print("done.")
  }
}
