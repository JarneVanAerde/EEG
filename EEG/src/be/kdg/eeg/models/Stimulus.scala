package be.kdg.eeg.models

class Stimulus(val stimType: StimulusType, val word: String, val measures: Vector[Vector[ContactPointValue]]) {
}
