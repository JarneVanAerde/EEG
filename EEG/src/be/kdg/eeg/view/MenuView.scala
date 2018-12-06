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

  //Nodes
  private val btnSlidingWindow = new Button(SLIDING_WINDOW, IMG_SLIDING_WINDOW)
  private val btnRegularChart = new Button(REGULAR_CHART, IMG_REGULAR_CHART)
  private val lblSubText = new Label(SLOGAN)
  private val line = new Line(100, 0, 400, 0)
  private val hboxButtons = new HBox(btnRegularChart, btnSlidingWindow)
  private val vboxLogo = new VBox(LOGO, line, lblSubText)


  layoutNodes()
  addCssSelectors()

  def layoutNodes() = {
    val topPane = new BorderPane(vboxLogo)
    val bottomPane = new BorderPane(hboxButtons)
    val contentVbox = new VBox(topPane, bottomPane)
    contentVbox.setAlignment(Pos.CENTER)
    this.setCenter(contentVbox)
  }

  def addCssSelectors(): Unit = {
    LOGO.getStyleClass.add("logo")
    lblSubText.getStyleClass.add("slogan")
    line.getStyleClass.add("logo-line")
    btnSlidingWindow.getStyleClass.add("menu-button")
    btnRegularChart.getStyleClass.add("menu-button")
    btnSlidingWindow.getStyleClass.add("sliding-button")
    hboxButtons.getStyleClass.add("btn-container")
    vboxLogo.getStyleClass.add("logo-container")
  }

  def getBtnSlidingWindow: Button = btnSlidingWindow

  def getBtnRegularChart: Button = btnRegularChart

  def getLblSubText: Label = lblSubText
}
