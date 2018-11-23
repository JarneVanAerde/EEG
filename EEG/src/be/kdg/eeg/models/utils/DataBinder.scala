package be.kdg.eeg.models.utils

class DataBinder(val fileForStimulus: String) {
  val unParsedData: Array[Array[String]] = new FileLoader(fileForStimulus).unParsedData

  def getOfferdStrings: Vector[String] = {
    unParsedData.filter(_ (0).toLowerCase.contains("stimulus"))
      .map(_ (1)).toVector
  }

  def getParsedData: Vector[Map[String, Vector[String]]] = {
    //Get stimuli
    //val parsedData = Vector[Map[String, Vector[String]]] =
    unParsedData.filter(_.length <= 2).foreach(
      _.filter(!_.contains("stimulus")).foreach(println(_))
    )

    //get code point values

    null
  }

}
