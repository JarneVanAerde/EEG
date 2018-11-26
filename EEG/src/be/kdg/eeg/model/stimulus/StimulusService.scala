package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.shared.DataBinder

class StimulusService(val fileForStimulus: String) {
  val stimuli: Vector[Stimulus] = new DataBinder(fileForStimulus).getParsedData

}
