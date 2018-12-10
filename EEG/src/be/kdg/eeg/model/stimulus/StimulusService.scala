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
class StimulusService(val fileForStimulus: String, val nameOfPerson: String) {
  final val analyseTools: AnalysisTools = new AnalysisTools(this)
  final val stimuli: Vector[Stimulus] = new DataBinder(fileForStimulus).getParsedData
  final val outlierFreeStimuli: Future[Vector[Stimulus]] = Future {
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
      if (optional.isEmpty) return stimuli

      optional.get.get
    }
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
    if (verbs) getData.filter(_.stimType == StimulusType.VERB)
    else getData.filter(_.stimType == StimulusType.NOUN)
  }
}
