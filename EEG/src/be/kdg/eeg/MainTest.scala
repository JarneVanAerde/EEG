package be.kdg.eeg

import java.time.LocalTime
import java.time.temporal.ChronoUnit

import be.kdg.eeg.model.stimulus.StimulusServiceStore


object MainTest {
  def main(args: Array[String]): Unit = {
    val oldTime = LocalTime.now()
    val store: StimulusServiceStore = new StimulusServiceStore()
    val test = store.getService("Bart").analyseTools.getInterestingData("geef",
      "F4", useAvg = false, slidingWindowSize = 6, minRange = 2, maxRange = 3)
    print(ChronoUnit.MILLIS.between(oldTime, LocalTime.now()))
  }
}
