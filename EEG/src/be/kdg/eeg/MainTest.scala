package be.kdg.eeg
import be.kdg.eeg.utils.FileLoader

object MainTest {
  def main(args: Array[String]): Unit = {
    new FileLoader("src/be/kdg/eeg/resources/Barbara_NounVerb.csv").loadFile()
  }
}
