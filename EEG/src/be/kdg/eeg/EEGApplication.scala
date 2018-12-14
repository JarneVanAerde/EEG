package be.kdg.eeg

import be.kdg.eeg.model.stimulus.StimulusServiceStore
import be.kdg.eeg.presenter.MenuPresenter
import be.kdg.eeg.view.MenuView
import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.image.Image

object EEGApplication {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[EEGApplication], args: _*)
  }
}

final class EEGApplication extends Application {
  private final val STAGE_MIN_HEIGHT = 768
  private final val STAGE_MIN_WIDTH = 1024
  private final val STAGE_TITLE = "EEG Application"
  private final val STYLE_SHEET_PATH = "be/kdg/eeg/view/css/stylesheet.css"
  private final val ICON = "be/kdg/eeg/view/img/icon.png"
  private val store = new StimulusServiceStore

  override def start(primaryStage: Stage): Unit = {
    val view = new MenuView()
    new MenuPresenter(view, store)

    val scene = new Scene(view)
    scene.getStylesheets.add(STYLE_SHEET_PATH)

    primaryStage.getIcons.add(new Image(ICON))
    primaryStage.setTitle(STAGE_TITLE)
    primaryStage.setScene(scene)
    import javafx.stage.Screen
    val screen = Screen.getPrimary
    val bounds = screen.getVisualBounds
    primaryStage.setX(bounds.getMinX)
    primaryStage.setY(bounds.getMinY)
    primaryStage.setWidth(bounds.getWidth)
    primaryStage.setHeight(bounds.getHeight)
    primaryStage.setMinWidth(bounds.getWidth)
    primaryStage.setMaxWidth(bounds.getWidth)
    primaryStage.setMinHeight(bounds.getHeight)
    primaryStage.setMaxHeight(bounds.getHeight)
    primaryStage.show()
  }
}