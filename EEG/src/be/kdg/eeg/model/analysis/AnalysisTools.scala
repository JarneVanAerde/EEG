package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.stimulus.StimulusService

/**
  * These tools are used for analysis on the contact points of specific stimuli
  * @param stimulusService The stimulusService used by the analysis
  */
class AnalysisTools(val stimulusService: StimulusService) {
  /**
    * @param stimulusString The string of the stimulus
    * @param contactPointString The contact points that need to be returned
    * @return The average of a specific set of contact points
    */
  def getAvgForContactPoints(stimulusString: String, contactPointString: String): Double = {
    val contactPoints = stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString)
    contactPoints.sum / contactPoints.length
  }

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
