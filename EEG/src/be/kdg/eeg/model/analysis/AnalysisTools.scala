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
  private final val MAX_SLIDING_WINDOW_TRESHHOLD: Double = 1.01
  private final val MIN_SLIDING_WINDOW_TRESHHOLD: Double = 1.02
  private final val MAX_OUTLIER_TRESHHOLD: Double = 1.5
  private final val MIN_OUTLIER_TRESHHOLD: Double = 2
  private final val OUTLIER_REPLACEMENT_RANGE: Int = 5

  def filterOutliersAndGetData(stimuliToFilter: Vector[Stimulus]): Vector[Stimulus] = {
    stimuliToFilter.map(stim => new Stimulus(stim.stimType, stim.word,
      filterAllContactPoints(stim.word, stim.measures, avgs = getAvgsForContactPoints(stim.word, stim.measures))))
  }

  private def filterAllContactPoints(stimulusString: String, curContactPoints: Vector[Vector[ContactPointValue]],
                                     newContactPoints: Vector[Vector[ContactPointValue]] = Vector[Vector[ContactPointValue]](),
                                     avgs: Vector[Double],
                                     counter: Int = 0): Vector[Vector[ContactPointValue]] = {
    if (counter < curContactPoints.length) {
      val mergedContactPoints = newContactPoints :+ filterContactPoints(avgs, stimulusString, curContactPoints(counter), outerCounter = counter)
      filterAllContactPoints(stimulusString, curContactPoints, mergedContactPoints, avgs, counter + 1)
    } else newContactPoints
  }

  private def filterContactPoints(avgs: Vector[Double], stimulusWord: String,
                                  curContactPoints: Vector[ContactPointValue],
                                  newContactPoints: Vector[ContactPointValue] = Vector[ContactPointValue](),
                                  innerCounter: Int = 0, outerCounter: Int
                                 ): Vector[ContactPointValue] = {
    if (innerCounter < curContactPoints.length) {
      val curPoint = curContactPoints(innerCounter)

      if ((avgs(innerCounter) * MAX_OUTLIER_TRESHHOLD) < curPoint.value ||
        (avgs(innerCounter) / MIN_SLIDING_WINDOW_TRESHHOLD) > curPoint.value) {
       val newAvg: Double = stimulusService.getContactPointValuesForStimulus(stimulusWord, curPoint.contactPoint)
         .slice(outerCounter - OUTLIER_REPLACEMENT_RANGE, outerCounter).sum / OUTLIER_REPLACEMENT_RANGE
        val mergedContactPoints = newContactPoints :+
          new ContactPointValue(curPoint.contactPoint, newAvg, curPoint.pos)
        filterContactPoints(avgs, stimulusWord, curContactPoints, mergedContactPoints, innerCounter + 1, outerCounter)
      } else {
        val mergedContactPoints = newContactPoints :+ curPoint
        filterContactPoints(avgs, stimulusWord, curContactPoints, mergedContactPoints, innerCounter + 1, outerCounter)
      }
    } else newContactPoints
  }


  private def getAvgsForContactPoints(stimulusString: String, contactPoints: Vector[Vector[ContactPointValue]],
                                      counter: Int = 0, avgs: Vector[Double] = Vector[Double]()): Vector[Double] = {
    if (counter < contactPoints(0).length) {
      val new_avgs = avgs :+ getAvgForContactPoints(stimulusString, contactPoints(0)(counter).contactPoint)
      getAvgsForContactPoints(stimulusString, contactPoints, counter + 1, new_avgs)
    } else avgs
  }

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
                                  pos: Vector[Int] = Vector[Int](), counter: Int = 0): Vector[Int] = {
    if (counter <= points.length - size) {
      val windowAvg = points.slice(counter, counter + size).sum / size

      //determine interesting data
      if ((rangeAvg * MAX_SLIDING_WINDOW_TRESHHOLD) < windowAvg || (rangeAvg / MIN_SLIDING_WINDOW_TRESHHOLD) > windowAvg) {
        val new_pos = pos :+ counter
        getSlidingWindowPos(points, size, rangeAvg, new_pos, counter + size)
      } else getSlidingWindowPos(points, size, rangeAvg, pos, counter + 1)

    } else pos
  }
}
