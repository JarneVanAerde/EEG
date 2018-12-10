package be.kdg.eeg.model.shared

import be.kdg.eeg.model.contactPoint.ContactPointValue
import be.kdg.eeg.model.stimulus.{Stimulus, StimulusType}

/**
  * Used to bind the unparsed data to the stimuli objects.
  *
  * @param fileForStimulus the file it needs to bind.
  */
class DataBinder(val fileForStimulus: String) {
  private val fileLoader: FileLoader = new FileLoader(fileForStimulus)
  private val unParsedData: Array[Array[String]] = fileLoader.loadFile
  private val unParsedPositions: Array[String] = fileLoader.loadOrdinairyFile("files/positions.txt")
  private val verbs: Array[String] = fileLoader.loadOrdinairyFile("files/verbs.txt")
  private val header: Array[String] = unParsedData(0)

  /**
    * The function zipWithIndex will let us know at which index
    * the different stimuli words are located.
    *
    * @return gives back the parsed data as stimuli objects.
    */
  def getParsedData: Vector[Stimulus] = {
    val zip = unParsedData.zipWithIndex.filter(x => x._1.length <= 2)
    val parsedData = zip.map(stim => new Stimulus(if (verbs.contains(stim._1(1))) StimulusType.VERB.toString else StimulusType.NOUN.toString,
      stim._1(1), getCodePointsForStimulus(stim._2 + 1))).toVector
    parsedData
  }

  /**
    * Recursive function that will fill an empty vector gradually.
    * A counter will be incremented for each row we go through.
    *
    * @param counter Represents the position in the document.
    * @param values  Empty vector that will fill gradually.
    * @return A parsed 2d vector of contact points.
    */
  private def getCodePointsForStimulus(counter: Int, values: Vector[Vector[ContactPointValue]] = Vector[Vector[ContactPointValue]]()): Vector[Vector[ContactPointValue]] = {
    if (counter < unParsedData.length && !unParsedData(counter)(0).contains("stimulus")) {
      val new_values = values :+ getCodePointsForStimulusRow(unParsedData(counter))
      getCodePointsForStimulus(counter + 1, new_values)
    } else values
  }

  /**
    * The same as the function above, but this time.
    * its at row-level instead of document-level.
    *
    * We only want the data between the indexes of 4 and 17
    * A header will be added to the data.
    *
    * @param row        The row with unparsed data.
    * @param row_values The empty array that will fill gradually.
    * @param counter    Represents the position in the row.
    * @return A parsed vector of contact points.
    */
  private def getCodePointsForStimulusRow(row: Array[String], row_values: Vector[ContactPointValue] = Vector[ContactPointValue](), counter: Int = 3): Vector[ContactPointValue] = {
    if (counter <= 16) {
      val new_row_values = row_values :+ new ContactPointValue(header(counter), row(counter).toDouble, unParsedPositions(counter - 3))
      getCodePointsForStimulusRow(row, new_row_values, counter + 1)
    } else row_values
  }



}
