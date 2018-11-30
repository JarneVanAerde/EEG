package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.analysis.AnalysisTools
import be.kdg.eeg.model.shared.DataBinder

/**
  * This service will communicate with the front-end application
  * to provide data for the graphs.
  *
  * @param fileForStimulus the file it does its operations on
  */
class StimulusService(val fileForStimulus: String, val nameOfPerson: String, val outlierReplaceRange: Int = 5) {
  val analyseTools: AnalysisTools = new AnalysisTools(this)
  val stimuli: Vector[Stimulus] = analyseTools.filterOutliersAndGetData(new DataBinder(fileForStimulus).getParsedData, outlierReplaceRange: Int)

  /**
    * @return A vector of all contact points by name
    */
  def getAllContactPointNames: Vector[String] = {
    stimuli(1).measures(1).map(_.contactPoint)
  }

  /**
    * @param stimulusString The string of a stimulus
    * @return The stimulus objects that corresponds with the
    */
  def getStimulus(stimulusString: String): Stimulus = {
    stimuli.find(_.word.equalsIgnoreCase(stimulusString))
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
    if (verbs) stimuli.filter(_.stimType == StimulusType.VERB)
    else stimuli.filter(_.stimType == StimulusType.NOUN)
  }
}
