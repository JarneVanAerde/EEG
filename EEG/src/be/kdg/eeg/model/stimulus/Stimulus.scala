package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.contactPoint.ContactPointValue

class Stimulus(val stimType: StimulusType, val word: String, val measures: Vector[Vector[ContactPointValue]]) {
}
