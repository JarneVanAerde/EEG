package be.kdg.eeg

import be.kdg.eeg.model.analysis.AnalysisTools
import be.kdg.eeg.model.stimulus.StimulusService

object MainTest {
  def main(args: Array[String]): Unit = {
    val service = new StimulusService("files/Barbara_NounVerb.csv")
    val analysisTools = new AnalysisTools(service)
    val test = analysisTools.getSlidingWindowAvgs("televisie", "AF3", 400)
    print("done.")
  }
}
