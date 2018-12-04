package be.kdg.eeg

import java.io.File

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}


object MainTest {
  def main(args: Array[String]): Unit = {
    val store: StimulusServiceStore = new StimulusServiceStore()
    val test = store.getService("Bart").getContactPointValuesForStimulus("televisie", "AF3")
    print("done.")
  }
}
