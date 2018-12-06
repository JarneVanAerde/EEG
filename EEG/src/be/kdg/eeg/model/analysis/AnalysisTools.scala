package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.contactPoint.ContactPointValue
import be.kdg.eeg.model.stimulus.{Stimulus, StimulusService}

/**
  * These tools are used for analysis on the contact points of specific stimuli
  *
  * @param stimulusService The stimulusService used by the analysis.
  */
class AnalysisTools(val stimulusService: StimulusService) {
  private final val RANGE_SEPERATOR: Int = 4
  private final val MAX_SLIDING_WINDOW_TRESHHOLD: Double = 1.005
  private final val MIN_SLIDING_WINDOW_TRESHHOLD: Double = 1.01

  def filterOutliersAndGetData(stimuliToFilter: Vector[Stimulus], filterRadius: Int): Vector[Stimulus] = {
    //stimuliToFilter.map(stim => new Stimulus(stim.stimType, stim.word,
    //filterAllContactPoints(stim.word, stim.measures)))
    stimuliToFilter
  }

  /*
  private def filterAllContactPoints(stimulusString: String, curContactPoints: Vector[Vector[ContactPointValue]],
                                     newContactPoints: Vector[Vector[ContactPointValue]] = Vector[Vector[ContactPointValue]](),
                                     avgs: Vector[Double] = Vector[Double](),
                                     counter: Int = 0): Vector[Vector[ContactPointValue]] = {
    if (counter > curContactPoints.length) {
      val avgs: Vector[Double] = if (avgs == null) curContactPoints.map(getAvgForContactPoints(stimulusString, _)) else avgs
      val mergedContactPoints = newContactPoints :+ filterContactPoints(avgs, curContactPoints(counter))
      filterAllContactPoints(stimulusString, curContactPoints, newContactPoints, avgs, counter + 1)
    } else newContactPoints
  }

  private def filterContactPoints(avgs: Vector[Double], newContactPoints: Vector[ContactPointValue] = Vector[ContactPointValue]()): Unit = {
    //TODO
  }
  */

  /**
    * @param stimulusString     The word of the stimulus.
    * @param contactPointString The contact points that need to be returned.
    * @return The average of a specific set of contact points.
    */
  def getAvgForContactPoints(stimulusString: String, contactPointString: String): Double = {
    val contactPoints = stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString)
    contactPoints.sum / contactPoints.length
  }

  /**
    * Calculates sliding window averages for all the contact points from a given.
    * stimulus object.
    *
    * @param stimulusString     The word of the stimulus.
    * @param contactPointString The name of the contact point.
    * @param slidingWindowSize  the size of the sliding window, this will effect the average calculation.
    * @return A vector of sliding window averages.
    */
  def getInterestingData(stimulusString: String, contactPointString: String,
                         minRange: Int = 0, maxRange: Int = 4, slidingWindowSize: Int = 3): Vector[Int] = {
    getSlidingWindowPos(stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString),
      slidingWindowSize, getRangeAvg(stimulusString, contactPointString, minRange, maxRange))
  }


  /**
    * Gets the average of the contactPoints
    * of a specific range.
    *
    * @param stimulusString     The word of the stimulus.
    * @param contactPointString The name of the contact point.
    * @param minRange           min range of average calculation.
    * @param maxRange           max range of average calculation.
    * @return the average of a contact point range from a specific stimulus object.
    */
  private def getRangeAvg(stimulusString: String, contactPointString: String, minRange: Int = 0, maxRange: Int = 4): Double = {
    val contactPoints: Vector[Double] = stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString)
    val rangeSlice: Int = contactPoints.length / RANGE_SEPERATOR
    val minRangeSlice: Int = rangeSlice * minRange
    val maxRangeSlice: Int = rangeSlice * maxRange

    contactPoints.slice(minRangeSlice, maxRangeSlice).sum / contactPoints.length
  }

  /**
    * Recursive function will calculate the average of each sliding window.
    * And determine if the data at that position is interesting. If that is the case
    * than the position will be added to the vector.
    *
    * @param points  The Contact points to calculate.
    * @param size    The size of the sliding window.
    * @param pos     An empty vector that will contain the averages of each sliding window.
    * @param counter Counter that will keep track of the sliding window steps.
    * @return A vector that contains al interesting data points.
    */
  private def getSlidingWindowPos(points: Vector[Double], size: Int, rangeAvg: Double,
                                  pos: Vector[Int] = Vector[Int](), counter: Int = 0, wasAdded: Boolean = false): Vector[Int] = {
    if (counter <= points.length - size) {
      val windowAvg = points.slice(counter, counter + size).sum / size

      //determine interesting data
      val checkIfPointWasAddedBefore = if (wasAdded) pos(pos.length - 1) + size > counter else true
      if (((rangeAvg * MAX_SLIDING_WINDOW_TRESHHOLD) < windowAvg || (rangeAvg / MIN_SLIDING_WINDOW_TRESHHOLD) > windowAvg)
        && checkIfPointWasAddedBefore) {
        val new_pos = pos :+ counter
        getSlidingWindowPos(points, size, rangeAvg, new_pos, counter + 1, true)
      } else getSlidingWindowPos(points, size, rangeAvg, pos, counter + 1, false)

    } else pos
  }
}
