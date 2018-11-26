package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.shared.DataBinder

class StimulusService(val fileForStimulus: String) {
  val stimuli: Vector[Stimulus] = new DataBinder(fileForStimulus).getParsedData

  def getStimuli(stimulusString: String): Stimulus = {
    stimuli.find(_.word.equalsIgnoreCase(stimulusString))
      .getOrElse(throw new Exception("Stimulus string was not found"))
  }

  def getContactPointValuesForStimulus(stimulusString: String, contactPointString: String): Vector[Double] = {
    val stimulus: Stimulus = stimuli.find(_.word.equalsIgnoreCase(stimulusString))
      .getOrElse(throw new Exception("Stimulus string was not found"))
    stimulus.measures.flatMap(_.filter(_.contactPoint.equalsIgnoreCase(contactPointString)).map(_.value))
  }

  def getAvgForContactPoints(stimulusString: String, contactPointString: String): Double = {
    val contactPoints = getContactPointValuesForStimulus(stimulusString, contactPointString)
    contactPoints.sum / contactPoints.length
  }
}
