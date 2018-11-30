package be.kdg.eeg.view

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.{Button, ComboBox, Label}
import javafx.scene.layout.{BorderPane, HBox, VBox}

class MenuView extends BorderPane {
  final val PADDING = new Insets(10, 10, 10, 10)
  val lblHeader = new Label("EEG Monitor")
  val btnSlidingWindow = new Button("Sliding Window")
  val btnRegularChart = new Button("Regular Chart")

  layoutNodes(lblHeader, btnSlidingWindow, btnRegularChart)

  def layoutNodes(lblHeader: Label, lblSlidingWindow: Button, lblRegularCharts: Button) = {
    val hbox = new HBox(lblSlidingWindow, lblRegularCharts)
    hbox.setAlignment(Pos.CENTER)
    hbox.setSpacing(10)
    lblHeader.getStyleClass.add("header")
    val headerPane = new BorderPane(lblHeader)
    headerPane.setPadding(new Insets(100, 0, -100, 0))
    this.setTop(headerPane)
    this.setCenter(new BorderPane(hbox))
  }
}
