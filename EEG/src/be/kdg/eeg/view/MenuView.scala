package be.kdg.eeg.view

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.{Button, ComboBox, Label}
import javafx.scene.image.ImageView
import javafx.scene.layout.{BorderPane, HBox, VBox}
import javafx.scene.shape.Line

class MenuView extends BorderPane {
  final val PADDING = new Insets(10, 10, 10, 10)
  final val LOGO = new ImageView("be/kdg/eeg/view/img/logo.png")
  final val IMG_SLIDING_WINDOW = new ImageView("be/kdg/eeg/view/img/sliding.png")
  final val IMG_REGULAR_CHART = new ImageView("be/kdg/eeg/view/img/regular.png")
  final val SLOGAN = "Jarne Van Aerde & Bryan de Ridder"
  final val SLIDING_WINDOW = "Sliding window"
  final val REGULAR_CHART = "Regular chart"

  val btnSlidingWindow = new Button(SLIDING_WINDOW, IMG_SLIDING_WINDOW)
  val btnRegularChart = new Button(REGULAR_CHART, IMG_REGULAR_CHART)
  val lblSubText = new Label(SLOGAN)

  layoutNodes(btnSlidingWindow, btnRegularChart, lblSubText)

  def layoutNodes(lblSlidingWindow: Button, lblRegularCharts: Button, lblSubText: Label) = {
    val hbox = new HBox(lblRegularCharts, lblSlidingWindow)
    hbox.setAlignment(Pos.CENTER)
    hbox.setSpacing(50)
    val line = new Line(100, 0, 400, 0)
    val vbox = new VBox(LOGO, line, lblSubText)
    vbox.setAlignment(Pos.CENTER)
    val topPane = new BorderPane(vbox)
    val bottomPane = new BorderPane(hbox)
    val contentVbox = new VBox(topPane, bottomPane)
    contentVbox.setAlignment(Pos.CENTER)
    this.setCenter(contentVbox)

    LOGO.getStyleClass.add("logo")
    lblSubText.getStyleClass.add("slogan")
    line.getStyleClass.add("logoLine")
    btnSlidingWindow.getStyleClass.add("menuButton")
    btnRegularChart.getStyleClass.add("menuButton")
    btnSlidingWindow.getStyleClass.add("slidingButton")
  }
}
