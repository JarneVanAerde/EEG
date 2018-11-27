package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.stimulus.StimulusService

class AnalysisTools(val stimulusService: StimulusService) {
  def getSlidingWindowAvgs(stimulusString: String, contactPointString: String, minRange: Int = 1, mexRange: Int = 4): Vector[Double] = {
    getSlidingWindowAvg(stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString))
  }

  private def getSlidingWindowAvg(points: Vector[Double], avgs: Vector[Double] = Vector[Double](), counter: Int = 0): Vector[Double] = {
    if (counter < points.length - 3) {
      val new_avgs = avgs :+ (points(counter) + points(counter + 1) + points(counter + 2)) / 3
      getSlidingWindowAvg(points, new_avgs, counter + 1)
    } else avgs
  }
}
