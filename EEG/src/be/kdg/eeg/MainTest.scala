package be.kdg.eeg

import be.kdg.eeg.models.utils.{DataBinder, FileLoader}

object MainTest {
  def main(args: Array[String]): Unit = {
    new DataBinder("files/Barbara_NounVerb.csv").getParsedData
  }
}
