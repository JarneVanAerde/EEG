package be.kdg.eeg

import java.time.LocalTime
import java.time.temporal.ChronoUnit

import be.kdg.eeg.model.stimulus.StimulusServiceStore


object MainTest {
  def main(args: Array[String]): Unit = {
    val oldTime = LocalTime.now()
    val store: StimulusServiceStore = new StimulusServiceStore()
    val test = store.getService("Barbara").analyseTools.getInterestingData("geef", "AF3", useAvg = false)
    print(ChronoUnit.MILLIS.between(oldTime, LocalTime.now()))
  }
}
