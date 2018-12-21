package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.analysis.AnalysisTools
import be.kdg.eeg.model.shared.DataBinder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This service will communicate with the front-end application
  * to provide data for the graphs.
  *
  * @param fileForStimulus the file it does its operations on.
  * @param nameOfPerson    the name of the person that the service is bound to.
  */
final class StimulusService(val fileForStimulus: String, val nameOfPerson: String) {
  val SINGLE_MEASURE_DURATION: Double = 7.8125
  val analyseTools: AnalysisTools = new AnalysisTools(this)
  val stimuli: Vector[Stimulus] = new DataBinder(fileForStimulus).getParsedData
  val outlierFreeStimuli: Future[Vector[Stimulus]] = Future {
    analyseTools.filterOutliersAndGetData(stimuli)
  }

  /**
    * Determines if the outlierFreeStimuli thread is ready.
    * If that is the case, then it will be returned.
    *
    * @return The correct data set
    */
  private def getData: Vector[Stimulus] = {
    if (!outlierFreeStimuli.isCompleted) {
      stimuli
    } else {
      val optional = outlierFreeStimuli.value
      if (optional.isEmpty || optional.get.isFailure) return stimuli
      optional.get.get
    }
  }

  /**
    * This function will calculate all the time frames in function of the length of the measurements.
    *
    * @param timeFrames The vector that will gradually be filled with time frames.
    * @param counter    The counter used by the function.
    * @return A vector of all the time frames.
    */
  def getTimeFrames(timeFrames: Vector[Double] = Vector[Double](), counter: Int = 1): Vector[Double] = {
    if (counter > stimuli(0).measures.size) return timeFrames

    val new_timeFrames = timeFrames :+ SINGLE_MEASURE_DURATION * counter
    getTimeFrames(new_timeFrames, counter + 1)
  }

  /**
    * @return A vector of all contact points by name
    */
  def getAllContactPointNames: Vector[String] = {
    getData(0).measures(0).map(_.contactPoint)
  }

  /**
    * @param stimulusString The string of a stimulus
    * @return The stimulus objects that corresponds with the
    */
  def getStimulus(stimulusString: String): Stimulus = {
    getData.find(_.word.equalsIgnoreCase(stimulusString))
      .getOrElse(throw new Exception("Stimulus string was not found"))
  }

  /**
    * Gets all the contact points for a specific contact point string.
    * By example: AF3, F7, etc.
    *
    * @param stimulusString     The string of the stimulus
    * @param contactPointString The contact points that need to be returned
    * @return A vector of all contact points.
    */
  def getContactPointValuesForStimulus(stimulusString: String, contactPointString: String): Vector[Double] = {
    getStimulus(stimulusString).measures
      .flatMap(_.filter(_.contactPoint.equalsIgnoreCase(contactPointString)).map(_.value))
  }

  /**
    * Gives back all the stimuli objects that have the same
    *
    * @param verbs Is true if you want the verbs and false if you want he nouns
    */
  def getStimulusTypes(verbs: Boolean): Vector[Stimulus] = {
    if (verbs) getData.filter(hasStimType(StimulusType.VERB))
    else getData.filter(hasStimType(StimulusType.NOUN))
  }

  /**
    * @param stimType The type of stimulus it nee
    * @param stimulus This parameter will be passed implicitly by the filter method.
    * @return true if the function returns true.
    */
  def hasStimType(stimType: StimulusType.Value)(stimulus: Stimulus): Boolean = {
    stimulus.stimType == stimType.toString
  }
}
