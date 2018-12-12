package be.kdg.eeg.view

import be.kdg.eeg.view.util.LineChartView
import javafx.scene.chart.NumberAxis
import javafx.scene.control.{Button, ComboBox, Label, Tooltip}
import javafx.scene.layout.{BorderPane, HBox, VBox}
import javafx.util.Duration

/**
  * Describes the view for the RegularChart
  */
class RegularChartView extends ChartView {
  //NODES
  private  val _chart: LineChartView[Number, Number] = {
    val xAxis = new NumberAxis(RegularChartView.X_AXIS_LABEL, 0, RegularChartView.MAX_TIME, RegularChartView.TICK_UNIT)
    xAxis.setAutoRanging(false)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(RegularChartView.Y_AXIS_LABEL)
    new LineChartView(xAxis, yAxis)
  }
  layoutNodes()

  override def layoutNodes(): Unit = {
    val bottomPane = layoutToolbar()
    this.setBottom(bottomPane)
    _chart.setTitle(RegularChartView.CHART_TITLE)
  }

  def layoutToolbar(): BorderPane = {
    val vboxData = new VBox(new Label(RegularChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(RegularChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(RegularChartView.CONTACT_POINT), _comboBoxContactPoint)
    val hbox = new HBox(vboxData, vboxStimulus, vboxContact, _btnAddData)
    hbox.getStyleClass.add("toolbar")
    this.setCenter(_chart)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
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
  def chart: LineChartView[Number, Number] = _chart
}

object RegularChartView {
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