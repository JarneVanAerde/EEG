package be.kdg.eeg.model.stimulus

import be.kdg.eeg.model.contactPoint.ContactPointValue

/**
  * Basic class used for storing all the data of a specif stimulus object.
  * The object holds all the contact points in a 2d vector that are related to that object
  *
  * @param stimType The type of word (NOUN, VERB) see StimulusType enum
  * @param word The word itself
  * @param measures All the measures for that word
  */
class Stimulus(val stimType: String, val word: String, val measures: Vector[Vector[ContactPointValue]]) {
}
