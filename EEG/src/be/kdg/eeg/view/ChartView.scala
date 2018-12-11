package be.kdg.eeg.view

import be.kdg.eeg.view.util.LineChartView
import javafx.scene.chart.NumberAxis
import javafx.scene.control.{Button, ComboBox, Label, Tooltip}
import javafx.scene.layout.{BorderPane, HBox, VBox}
import javafx.util.Duration

/**
  * Describes the view for the RegularChart
  */
class ChartView extends BorderPane {
  //NODES
  private val _comboBoxStimulus = new ComboBox[String]
  private val _comboBoxContactPoint = new ComboBox[String]
  private val _comboBoxPersonInput = new ComboBox[String]
  private val _btnClear = new Button(ChartView.CLEAR_CHART)
  private val _btnBack = new Button(ChartView.BACK)
  private val _btnAddData = new Button(ChartView.ADD_DATA)
  private val _tooltipBack = new Tooltip(ChartView.BACK_TOOLTIP)
  private val _tooltipClear = new Tooltip(ChartView.CLEAR_TOOLTIP)
  private val _tooltipAddData = new Tooltip(ChartView.ADD_DATA_TOOLIP)
  _tooltipBack.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipClear.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipAddData.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  private val _chart: LineChartView[Number, Number] = {
    val xAxis = new NumberAxis(ChartView.X_AXIS_LABEL, 0, ChartView.MAX_TIME, ChartView.TICK_UNIT)
    xAxis.setAutoRanging(false)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(ChartView.Y_AXIS_LABEL)
    new LineChartView(xAxis, yAxis)
  }

  layoutNodes()

  def layoutNodes(): Unit = {
    val bottomPane = layoutToolbar()
    this.setBottom(bottomPane)
    _chart.setTitle(ChartView.CHART_TITLE)
  }

  def layoutToolbar(): BorderPane = {
    val vboxData = new VBox(new Label(ChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(ChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(ChartView.CONTACT_POINT), _comboBoxContactPoint)
    val hbox = new HBox(vboxData, vboxStimulus, vboxContact, _btnAddData)
    hbox.getStyleClass.add("toolbar")
    this.setCenter(_chart)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    _btnBack.setTooltip(_tooltipBack)
    _btnClear.setTooltip(_tooltipClear)
    _btnAddData.setTooltip(_tooltipAddData)
    bottomLeftPane.setBottom(_btnBack)
    bottomRightPane.setBottom(_btnClear)
    bottomLeftPane.getStyleClass.add("bottom-left-toolbar")
    bottomRightPane.getStyleClass.add("bottom-right-toolbar")
    val bottomPane = new BorderPane(hbox)
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
  def chart: LineChartView[Number, Number] = _chart
}

object ChartView {
  //CONSTANTS
  val MAX_TIME: Double = 4000
  val TICK_UNIT: Double = 500
  val DATA = "Data:"
  val STIMULUS = "Stimulus:"
  val CONTACT_POINT = "Contact point:"
  val X_AXIS_LABEL = "Time (ms)"
  val Y_AXIS_LABEL = "Activity"
  val CLEAR_CHART = "Clear"
  val BACK = "Back"
  val ADD_DATA = "Add data"
  val BACK_TOOLTIP = "Back to menu"
  val CLEAR_TOOLTIP = "Clear the chart"
  val ADD_DATA_TOOLIP = "Add data to the chart"
  val CHART_TITLE = "Brain activity over time"
  val BUTTON_TOOLTIP_DELAY = new Duration(500)
}