package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.shared.DataBinder

/**
  * This service will communicate with the front-end application
  * to provide data for the graphs.
  *
  * @param fileForStimulus the file it does its operations on
  */
class StimulusService(val fileForStimulus: String) {
  val stimuli: Vector[Stimulus] = new DataBinder(fileForStimulus).getParsedData

  def getAllContactPointNames: Vector[String] ={
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
    * @param stimulusString The string of the stimulus
    * @param contactPointString The contact points that need to be returned
    * @return A vector of all contact points.
    */
  def getContactPointValuesForStimulus(stimulusString: String, contactPointString: String): Vector[Double] = {
    getStimulus(stimulusString).measures
      .flatMap(_.filter(_.contactPoint.equalsIgnoreCase(contactPointString)).map(_.value))
  }

  /**
    * @param stimulusString The string of the stimulus
    * @param contactPointString The contact points that need to be returned
    * @return The average of a specific set of contact points
    */
  def getAvgForContactPoints(stimulusString: String, contactPointString: String): Double = {
    val contactPoints = getContactPointValuesForStimulus(stimulusString, contactPointString)
    contactPoints.sum / contactPoints.length
  }
}
