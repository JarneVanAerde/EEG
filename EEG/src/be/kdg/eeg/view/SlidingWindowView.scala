package be.kdg.eeg.view

import be.kdg.eeg.view.util.{LineChartView, NumberField}
import javafx.scene.chart.NumberAxis
import javafx.scene.control._
import javafx.scene.layout.{AnchorPane, BorderPane, HBox, VBox}
import javafx.scene.shape.Rectangle

/**
  * Describes the view for the SlidingWindow
  */
class SlidingWindowView extends BorderPane {
  //NODES
  private val _comboBoxStimulus = new ComboBox[String]
  private val _comboBoxContactPoint = new ComboBox[String]
  private val _comboBoxPersonInput = new ComboBox[String]
  private val _btnClear = new Button(ChartView.CLEAR_CHART)
  private val _btnBack = new Button(ChartView.BACK)
  private val _btnAddData = new Button(ChartView.ADD_DATA)
  private val _btnAvgLine = new Button(SlidingWindowView.AVERAGE_LINE)
  private val _tooltipBack = new Tooltip(ChartView.BACK_TOOLTIP)
  private val _tooltipClear = new Tooltip(ChartView.CLEAR_TOOLTIP)
  private val _tooltipAddData = new Tooltip(ChartView.ADD_DATA_TOOLIP)
  private val _fldWindowSize = new NumberField(0,10)
  private val _window = new Rectangle()
  _tooltipBack.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipClear.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipAddData.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  private val _chart: LineChartView[Number, Number] = {
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
    val vboxData = new VBox(new Label(ChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(ChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(ChartView.CONTACT_POINT), _comboBoxContactPoint)
    val vboxWindowSize = new VBox(new Label(SlidingWindowView.WINDOW_SIZE), _fldWindowSize)
    val hBoxToolbar = new HBox(vboxData, vboxStimulus, vboxContact, vboxWindowSize, _btnAvgLine, _btnAddData)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    _btnBack.setTooltip(_tooltipBack)
    _btnClear.setTooltip(_tooltipClear)
    _btnAddData.setTooltip(_tooltipAddData)
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
  def comboBoxStimulus: ComboBox[String] = _comboBoxStimulus
  def comboBoxContactPoint: ComboBox[String] = _comboBoxContactPoint
  def comboBoxPersonInput: ComboBox[String] = _comboBoxPersonInput
  def btnClear: Button = _btnClear
  def btnBack: Button = _btnBack
  def btnAddData: Button = _btnAddData
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