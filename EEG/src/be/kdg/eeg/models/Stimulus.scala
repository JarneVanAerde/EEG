package be.kdg.eeg.models

class Stimulus(val stimType: StimulusType, val offerdString: String, val measures: Vector[Vector[ContactPointValue]]) {
}
