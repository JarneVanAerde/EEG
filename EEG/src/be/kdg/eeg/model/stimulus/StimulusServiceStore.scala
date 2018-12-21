package be.kdg.eeg.model.stimulus

import java.io.File

/**
  * This class is used to manage the all the stimulusServices.
  * Each service is bound to a specific document.
  */
final class StimulusServiceStore() {
  val fileNames: Vector[String] = getFileNames
  private val stimulusServices: Vector[StimulusService] = loadStimulusServices

  /**
    * @param extension we need to check.
    * @param file the file that is passed implicitly by the stream function.
    * @return true if the extension matches the extension we were looking for.
    */
  def hasExtension(extension: String)(file: File): Boolean = {
    file.toString.endsWith(extension) && file.isFile
  }

  /**
    * @param prefix
    * @param suffix
    * @param file
    * @return
    */
  def removeFromText(prefix: String)(suffix: String)(file: File): String = {
    file.toString.stripPrefix(prefix).stripSuffix(suffix)
  }

  /**
    * Uses reflection to determine the name of the files.
    *
    * @return A vector containing the name of the files.
    */
  def getFileNames: Vector[String] = {
    new File("files").listFiles
      .filter(hasExtension(".csv"))
      .map(removeFromText("files\\")("_NounVerb.csv"))
      .toVector
  }

  /**
    * Gets the names of the files and maps them to the
    * User services.
    *
    * @return A vector of all the stimulusServices.
    */
  private def loadStimulusServices: Vector[StimulusService] = {
    fileNames.map(name => new StimulusService("files/" + name + "_NounVerb.csv", name))
  }

  /**
    * @param name The name of the service.
    * @return The requested service.
    */
  def getService(name: String): StimulusService = {
    stimulusServices.find(_.nameOfPerson.equalsIgnoreCase(name)).
      getOrElse(throw new Exception("The given service name was not found in memory"))
  }
}
