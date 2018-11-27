package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.stimulus.StimulusService

class AnalysisTools(val stimulusService: StimulusService) {
  def getSlidingWindowAvgs(stimulusString: String, contactPointString: String, slidingWindowSize: Int, minRange: Int = 1, mexRange: Int = 4): Vector[Double] = {
    getSlidingWindowAvg(stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString), slidingWindowSize)
  }

  private def getSlidingWindowAvg(points: Vector[Double], size: Int, avgs: Vector[Double] = Vector[Double](), counter: Int = 0): Vector[Double] = {
    if (counter <= points.length - size) {
      val new_avgs = avgs :+ points.slice(counter, counter + size).sum / size
      getSlidingWindowAvg(points, size, new_avgs, counter + 1)
    } else avgs
  }
}
