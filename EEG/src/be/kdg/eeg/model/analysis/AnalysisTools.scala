package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.stimulus.StimulusService

/**
  * These tools are used for analysis on the contact points of specific stimuli
  * @param stimulusService The stimulusService used by the analysis
  */
class AnalysisTools(val stimulusService: StimulusService) {
  final val RANGE_SEPERATOR: Int = 4

  /**
    * @param stimulusString The word of the stimulus
    * @param contactPointString The contact points that need to be returned
    * @return The average of a specific set of contact points
    */
  def getAvgForContactPoints(stimulusString: String, contactPointString: String): Double = {
    val contactPoints = stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString)
    contactPoints.sum / contactPoints.length
  }

  /**
    * Gets the average of the contactPoints
    * of a specific range.
    *
    * @param stimulusString The word of the stimulus.
    * @param contactPointString The name of the contact point.
    * @param minRange min range of average calculation.
    * @param maxRange max range of average calculation.
    * @return the average of a contact point range from a specific stimulus object.
    */
  def getRangeAvg(stimulusString: String, contactPointString: String, minRange: Int = 0, maxRange: Int = 4): Double = {
    val contactPoints: Vector[Double] = stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString)
    val rangeSlice: Int = contactPoints.length / RANGE_SEPERATOR
    val minRangeSlice: Int = rangeSlice * minRange
    val maxRangeSlice: Int = rangeSlice * maxRange

    contactPoints.slice(minRangeSlice, maxRangeSlice).sum / contactPoints.length
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
