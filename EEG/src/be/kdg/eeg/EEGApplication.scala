package be.kdg.eeg

import be.kdg.eeg.presenters.StartPresenter
import be.kdg.eeg.views.StartView
import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene

object EEGApplication {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[EEGApplication], args: _*)
  }
}

class EEGApplication extends Application {
  private final val STAGE_MIN_HEIGHT = 768
  private final val STAGE_MIN_WIDTH = 1024
  private final val STAGE_TITLE = "EEG Application"
  private final val STYLE_SHEET_PATH = "be/kdg/eeg/view/css/stylesheet.css"

  override def start(primaryStage: Stage): Unit = {
    val view = new StartView()
    new StartPresenter(view)

    val scene = new Scene(view)
    scene.getStylesheets.add(STYLE_SHEET_PATH)

    primaryStage.setTitle(STAGE_TITLE)
    primaryStage.setScene(scene)
    primaryStage.setMinHeight(STAGE_MIN_HEIGHT)
    primaryStage.setMinWidth(STAGE_MIN_WIDTH)
    primaryStage.show()
  }
}