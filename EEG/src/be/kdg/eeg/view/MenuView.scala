package be.kdg.eeg.view

import javafx.geometry.Insets
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane

class MenuView extends BorderPane{
  final val PADDING = new Insets(10, 10, 10, 10)
  val textField = new Label("What would you like to do?")
  val button_slidingWindow = new Button("Sliding window")
  val button_testing = new Button("testing")
}
