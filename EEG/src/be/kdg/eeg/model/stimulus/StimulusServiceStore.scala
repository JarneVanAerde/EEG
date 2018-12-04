package be.kdg.eeg.model.stimulus

import java.io.File

/**
  * This class is used to manage the all the stimulusServices.
  * Each service is bound to a specific document.
  */
class StimulusServiceStore() {
  private val stimulusServices: Vector[StimulusService] = loadStimulusServices

  /**
    * Gets the names of the files with reflection and maps them to the
    * User services.
    *
    * @return A vector of all the stimulusServices.
    */
  private def loadStimulusServices: Vector[StimulusService] = {
    val nameOfFiles = new File("files").listFiles.filter(f => f.toString.endsWith(".csv") && f.isFile)
      .map(f => f.toString.stripPrefix("files\\").stripSuffix("_NounVerb.csv"))
    nameOfFiles.map(name => new StimulusService("files/" + name + "_NounVerb.csv", name)).toVector
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
