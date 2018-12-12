package be.kdg.eeg.view

import be.kdg.eeg.view.util.{LineChartView, NumberField}
import javafx.scene.chart.NumberAxis
import javafx.scene.control._
import javafx.scene.layout.{AnchorPane, BorderPane, HBox, VBox}
import javafx.scene.shape.Rectangle

/**
  * Describes the view for the SlidingWindow
  */
class SlidingWindowView extends ChartView {
  //NODES
  private val _btnAvgLine = new Button(SlidingWindowView.AVERAGE_LINE)
  private val _fldWindowSize = new NumberField(0, 10)
  private val _window = new Rectangle()
  private val _chart: LineChartView[Number, Number] = {
    val xAxis = new NumberAxis(RegularChartView.X_AXIS_LABEL, 0, RegularChartView.MAX_TIME, RegularChartView.TICK_UNIT)
    xAxis.setAutoRanging(false)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(RegularChartView.Y_AXIS_LABEL)
    val line = new LineChartView(xAxis, yAxis)
    line.setTitle(RegularChartView.CHART_TITLE)
    line.setAnimated(false)
    line
  }

  layoutNodes()

  def layoutNodes(): Unit = {
    val toolbarPane = layoutToolbar()
    val mainContainer = new BorderPane(_chart)
    mainContainer.setBottom(toolbarPane)
    _window.getStyleClass.add("window")
    //The (sliding) window is put on top of all the content.
    val anchorPane = new AnchorPane(mainContainer, _window)
    AnchorPane.setBottomAnchor(mainContainer, 0.0)
    AnchorPane.setTopAnchor(mainContainer, 0.0)
    AnchorPane.setLeftAnchor(mainContainer, 0.0)
    AnchorPane.setRightAnchor(mainContainer, 0.0)
    this.setCenter(anchorPane)
  }

  def layoutToolbar(): BorderPane = {
    val vboxData = new VBox(new Label(RegularChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(RegularChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(RegularChartView.CONTACT_POINT), _comboBoxContactPoint)
    val vboxWindowSize = new VBox(new Label(SlidingWindowView.WINDOW_SIZE), _fldWindowSize)
    val hBoxToolbar = new HBox(vboxData, vboxStimulus, vboxContact, vboxWindowSize, _btnAvgLine, _btnAddData)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    bottomLeftPane.setBottom(_btnBack)
    bottomRightPane.setBottom(_btnClear)
    bottomLeftPane.getStyleClass.add("bottom-left-toolbar")
    bottomRightPane.getStyleClass.add("bottom-right-toolbar")
    hBoxToolbar.getStyleClass.add("toolbar")
    val bottomPane = new BorderPane(hBoxToolbar)
    bottomPane.setLeft(bottomLeftPane)
    bottomPane.setRight(bottomRightPane)
    bottomPane
  }

  //GETTERS
  def btnAvgLine: Button = _btnAvgLine
  def window: Rectangle = _window
  def fldWindowSize: NumberField = _fldWindowSize
  def chart: LineChartView[Number, Number] = _chart
}

object SlidingWindowView {
  //CONSTANTS
  val AVERAGE_LINE = "Average line"
  val WINDOW_SIZE = "Window size"
}