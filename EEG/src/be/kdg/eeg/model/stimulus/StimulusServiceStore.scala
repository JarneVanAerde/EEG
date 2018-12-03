package be.kdg.eeg.model.stimulus

/**
  * This class is used to manage the all the stimulusServices.
  * Each service is bound to a specific document.
  *
  * @param serviceNames The names of the services we want to make. A default is also present.
  */
class StimulusServiceStore(val serviceNames: Vector[String] = Vector("Bart", "Barbara")) {
  private val stimulusServices: Vector[StimulusService] = loadStimulusServices

  /**
    * Bounds the names to the stimulusServices.
    *
    * @return A vector of all the stimulusServices.
    */
  private def loadStimulusServices: Vector[StimulusService] = {
     serviceNames.map(name => new StimulusService("files/" + name + "_NounVerb.csv", name))
  }

  /**
    * @param name The name of the service.
    * @return The requested service.
    */
  def getService(name: String): StimulusService = {
    stimulusServices.find(_.nameOfPerson.equalsIgnoreCase(name)).
      getOrElse(throw new Exception("The given serive name was not found in memory"))
  }
}
