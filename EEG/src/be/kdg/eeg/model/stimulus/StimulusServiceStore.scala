package be.kdg.eeg.model.stimulus

class StimulusServiceStore(val serviceNames: Vector[String]) {
  private val stimulusServices: Vector[StimulusService] = loadStimulusServices

  private def loadStimulusServices: Vector[StimulusService] = {
     serviceNames.map(name => new StimulusService("files/" + name + "_NounVerb.csv", name))
  }

  def getService(name: String): StimulusService = {
    stimulusServices.find(_.nameOfPerson.equalsIgnoreCase(name)).
      getOrElse(throw new Exception("The given serive name was not found in memory"))
  }
}
