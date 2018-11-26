package be.kdg.eeg.model.contactPoint

/**
  * Basic class used for storing a contact point of a specific stimulus object
  *
  * @param contactPoint The name of the contact point
  * @param value The value of the contact point
  * @param pos the position in the brian of the given contact point
  */
class ContactPointValue(val contactPoint: String, val value: Double, val pos: String) {
}
