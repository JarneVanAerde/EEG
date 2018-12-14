package be.kdg.eeg.view

import javafx.geometry.{Insets, Pos}
import javafx.scene.control.{Button, Label}
import javafx.scene.image.ImageView
import javafx.scene.layout.{BorderPane, HBox, VBox}
import javafx.scene.shape.Line

/**
  * Descrives the view for the main menu.
  */
class MenuView extends BorderPane {
  //NODES
  private val _btnSlidingWindow = new Button(MenuView.SLIDING_WINDOW, MenuView.IMG_SLIDING_WINDOW)
  private val _btnRegularChart = new Button(MenuView.REGULAR_CHART, MenuView.IMG_REGULAR_CHART)
  private val _lblSubText = new Label(MenuView.SLOGAN)
  private val _line = new Line(100, 0, 400, 0)
  private val _hboxButtons = new HBox(_btnRegularChart, _btnSlidingWindow)
  private val _vboxLogo = new VBox(MenuView.LOGO, _line, _lblSubText)


  layoutNodes()
  addCssSelectors()

  def layoutNodes(): Unit = {
    val topPane = new BorderPane(_vboxLogo)
    val bottomPane = new BorderPane(_hboxButtons)
    val contentVbox = new VBox(topPane, bottomPane)
    contentVbox.setAlignment(Pos.CENTER)
    this.setCenter(contentVbox)
  }

  def addCssSelectors(): Unit = {
    MenuView.LOGO.getStyleClass.add("logo")
    _lblSubText.getStyleClass.add("slogan")
    _line.getStyleClass.add("logo-line")
    _btnSlidingWindow.getStyleClass.add("menu-button")
    _btnRegularChart.getStyleClass.add("menu-button")
    _btnSlidingWindow.getStyleClass.add("sliding-button")
    _hboxButtons.getStyleClass.add("btn-container")
    _vboxLogo.getStyleClass.add("logo-container")
  }

  //GETTERS
  def btnSlidingWindow: Button = _btnSlidingWindow
  def btnRegularChart: Button = _btnRegularChart
  def lblSubText: Label = _lblSubText
}

object MenuView {
  //CONSTANTS
  val PADDING = new Insets(10, 10, 10, 10)
  val LOGO = new ImageView("be/kdg/eeg/view/img/logo.png")
  val IMG_SLIDING_WINDOW = new ImageView("be/kdg/eeg/view/img/sliding.png")
  val IMG_REGULAR_CHART = new ImageView("be/kdg/eeg/view/img/regular.png")
  val SLOGAN = "Jarne Van Aerde & Bryan de Ridder"
  val SLIDING_WINDOW = "Sliding window"
  val REGULAR_CHART = "Regular chart"
}