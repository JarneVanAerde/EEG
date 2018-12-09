package be.kdg.eeg.model.analysis

import be.kdg.eeg.model.contactPoint.ContactPointValue
import be.kdg.eeg.model.stimulus.{Stimulus, StimulusService}

/**
  * These tools are used for analysis on the contact points of specific stimuli
  *
  * @param stimulusService The stimulusService used by the analysis.
  */
class AnalysisTools(val stimulusService: StimulusService) {
  private final val RANGE_SEPARATOR: Int = 4
  private final val MAX_SLIDING_WINDOW_AVG_THRESHOLD: Double = 1.01
  private final val MIN_SLIDING_WINDOW_AVG_THRESHOLD: Double = 1.02
  private final val SLIDING_WINDOW_STD_THRESHOLD: Double = 0.01
  private final val MAX_OUTLIER_THRESHOLD: Double = 1.5
  private final val MIN_OUTLIER_THRESHOLD: Double = 2
  private final val OUTLIER_REPLACEMENT_RANGE: Int = 1

  /**
    * Gives back a filtered version of all the stimuli
    * without the outliers.
    *
    * @param stimuliToFilter The original stimuli
    * @return A vector of the filtered stimuli.
    */
  def filterOutliersAndGetData(stimuliToFilter: Vector[Stimulus]): Vector[Stimulus] = {
    stimuliToFilter.map(stim => new Stimulus(stim.stimType, stim.word,
      filterAllContactPoints(stim.word, stim.measures, avgs = getAvgsForContactPoints(stim.word, stim.measures))))
  }

  /**
    * This method will filter all the outliers form the contactPoints vector for a
    * specific stimulus object.
    *
    * @param stimulusString   The stimulus word.
    * @param curContactPoints The contact points of the stimulus.
    * @param newContactPoints The new filtered contact points.
    * @param avgs             A vector of all the contactPoint averages.
    * @param counter          Used by the recursive function.
    * @return A 2de vector of the filtered contact points.
    */
  private def filterAllContactPoints(stimulusString: String, curContactPoints: Vector[Vector[ContactPointValue]],
                                     newContactPoints: Vector[Vector[ContactPointValue]] = Vector[Vector[ContactPointValue]](),
                                     avgs: Vector[Double],
                                     counter: Int = 0): Vector[Vector[ContactPointValue]] = {
    if (counter < curContactPoints.length) {
      val mergedContactPoints = newContactPoints :+ filterContactPoints(avgs, stimulusString, curContactPoints(counter), outerCounter = counter)
      filterAllContactPoints(stimulusString, curContactPoints, mergedContactPoints, avgs, counter + 1)
    } else newContactPoints
  }

  /**
    * Filters all the contactPoints from a specific row. Each element of the row
    * will be checked on.
    *
    * @param avgs             A vector of contactPoint averages.
    * @param stimulusWord     The stimulus word.
    * @param curContactPoints The current contactPoints.
    * @param newContactPoints The filtered contact points.
    * @param innerCounter     The counter of the this function.
    * @param outerCounter     The counter of the funtion that called this function.
    * @return A manipulated row of data.
    */
  private def filterContactPoints(avgs: Vector[Double], stimulusWord: String,
                                  curContactPoints: Vector[ContactPointValue],
                                  newContactPoints: Vector[ContactPointValue] = Vector[ContactPointValue](),
                                  innerCounter: Int = 0, outerCounter: Int
                                 ): Vector[ContactPointValue] = {
    if (innerCounter < curContactPoints.length) {
      val curPoint = curContactPoints(innerCounter)

      if ((avgs(innerCounter) * MAX_OUTLIER_THRESHOLD) < curPoint.value || (avgs(innerCounter) / MIN_OUTLIER_THRESHOLD) > curPoint.value) {
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

  /**
    * Calculates the averages of each row of the
    * contact points vector.
    *
    * @param stimulusString The stimulus word.
    * @param contactPoints  Points that are used for the average calculation.
    * @param counter        The counter used by the recursive function
    * @param avgs           The averages vector that will gradually be filled
    * @return A vector of calculated averages.
    */
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
                         minRange: Int = 0, maxRange: Int = 4, slidingWindowSize: Int = 3, useAvg: Boolean = true): Vector[(Int, Int)] = {
    getSlidingWindowPos(stimulusService.getContactPointValuesForStimulus(stimulusString, contactPointString), slidingWindowSize,
      getRangeAvg(stimulusString, contactPointString, minRange, maxRange), useAvg = useAvg)
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
    val rangeSlice: Int = contactPoints.length / RANGE_SEPARATOR
    val minRangeSlice: Int = rangeSlice * minRange
    val maxRangeSlice: Int = rangeSlice * maxRange

    contactPoints.slice(minRangeSlice, maxRangeSlice).sum / contactPoints.length
  }

  /**
    * Recursive function will calculate the average or standard deviation of each sliding window.
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
                                  pos: Vector[(Int, Int)] = Vector[(Int, Int)](), counter: Int = 0, useAvg: Boolean = true): Vector[(Int, Int)] = {
    if (counter <= points.length - size) {
      val windowAvg = points.slice(counter, counter + size).sum / size

      //determine interesting data
      val succeededAvg = (rangeAvg * MAX_SLIDING_WINDOW_AVG_THRESHOLD) < windowAvg || (rangeAvg / MIN_SLIDING_WINDOW_AVG_THRESHOLD) > windowAvg
      val succeededStd = if (!useAvg) windowAvg > (rangeAvg + (getStandardDiv(points) * SLIDING_WINDOW_STD_THRESHOLD)) ||
        (windowAvg < rangeAvg - (getStandardDiv(points) * SLIDING_WINDOW_STD_THRESHOLD)) else true
      if (succeededAvg && succeededStd) {
        val new_pos = pos :+ (counter, counter + size - 1)
        getSlidingWindowPos(points, size, rangeAvg, new_pos, counter + size, useAvg)
      } else getSlidingWindowPos(points, size, rangeAvg, pos, counter + 1, useAvg)

    } else pos
  }

  /**
    * Calculates the standard deviation for a set of
    * contactPoints
    *
    * @return A standard deviation.
    */
  private def getStandardDiv[T: Numeric](xs: Iterable[T]): Double = {
    val avg = xs.sum.toString.toDouble / xs.size
    xs.map(x => x.toString.toDouble).map(a => math.pow(a - avg, 2)).sum / xs.size
  }


}
