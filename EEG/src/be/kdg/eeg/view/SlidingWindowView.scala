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
  private val _comboBoxCalcTechnique = new ComboBox[String]
  private val _fldMinRange = new NumberField(-1, 4)
  private val _fldMaxRange = new NumberField(-1, 4)
  private val _fldWindowSpeed = new NumberField(0,100)
  private val _fldWindowSize = new NumberField(0, 10)
  private val _window = new Rectangle()
  private val _chart: LineChartView = {
    val xAxis = new NumberAxis(ChartView.X_AXIS_LABEL, 0, ChartView.MAX_TIME, ChartView.TICK_UNIT)
    xAxis.setAutoRanging(false)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(ChartView.Y_AXIS_LABEL)
    val line = new LineChartView(xAxis, yAxis)
    line.setTitle(ChartView.CHART_TITLE)
    line.setAnimated(false)
    line
  }
  _fldWindowSpeed.setTooltip(new Tooltip(SlidingWindowView.TOOLTIP_SPEED))
  _fldWindowSize.setTooltip(new Tooltip(SlidingWindowView.TOOLTIP_SIZE))
  //TODO: fix highlighting
  fldMinRange.setDisable(true)
  fldMaxRange.setDisable(true)

  layoutNodes()

  override def layoutNodes(): Unit = {
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
    val vboxData = new VBox(new Label(ChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(ChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(ChartView.CONTACT_POINT), _comboBoxContactPoint)
    val vboxWindowSize = new VBox(new Label(SlidingWindowView.WINDOW_SIZE), _fldWindowSize)
    val vboxMinRange = new VBox(new Label(SlidingWindowView.MIN_RANGE), _fldMinRange)
    val vboxMaxRange = new VBox(new Label(SlidingWindowView.MAX_RANGE), _fldMaxRange)
    val vboxCalcTechnique = new VBox(new Label(SlidingWindowView.CALC_TECHNIQUE), _comboBoxCalcTechnique)
    val vboxSpeed = new VBox(new Label(SlidingWindowView.SPEED), _fldWindowSpeed)
    val hboxToolbar = new HBox(vboxData, vboxStimulus, vboxContact, vboxCalcTechnique, vboxWindowSize,
      vboxMinRange, vboxMaxRange, vboxSpeed, _btnAvgLine)
    val vboxRightToolbar = new VBox(_btnPlay, _btnClear)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    bottomLeftPane.setBottom(_btnBack)
    bottomRightPane.setBottom(vboxRightToolbar)
    vboxRightToolbar.getStyleClass.add("right-toolbar")
    bottomLeftPane.getStyleClass.add("bottom-left-toolbar")
    bottomRightPane.getStyleClass.add("bottom-right-toolbar")
    hboxToolbar.getStyleClass.add("toolbar")
    val bottomPane = new BorderPane(hboxToolbar)
    bottomPane.setLeft(bottomLeftPane)
    bottomPane.setRight(bottomRightPane)
    bottomPane
  }

  //GETTERS
  def btnAvgLine: Button = _btnAvgLine
  def window: Rectangle = _window
  def fldWindowSize: NumberField = _fldWindowSize
  def fldWindowSpeed: NumberField = _fldWindowSpeed
  def chart: LineChartView = _chart
  def comboBoxCalcTechnique: ComboBox[String] = _comboBoxCalcTechnique
  def fldMinRange: NumberField = _fldMinRange
  def fldMaxRange: NumberField = _fldMaxRange
}

object SlidingWindowView {
  //CONSTANTS
  val MAX_RANGE = "Max range:"
  val MIN_RANGE = "Min range:"
  val CALC_TECHNIQUE = "Calculation technique:"
  val AVERAGE_LINE = "Average line"
  val WINDOW_SIZE = "Size:"
  val TOOLTIP_SIZE = "Size of the sliding window"
  val SPEED = "Speed:"
  val TOOLTIP_SPEED = "Speed of the sliding window"
}