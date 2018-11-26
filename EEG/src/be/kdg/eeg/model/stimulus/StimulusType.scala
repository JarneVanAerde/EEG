package be.kdg.eeg.model.stimulus

/**
  * An enum that represents the type of word
  * (NOUN or VERB)
  */
class StimulusType extends Enumeration {
  type wordType = Value
  val VERB: Value = Value(1)
  val NOUN: Value = Value(2)
}
