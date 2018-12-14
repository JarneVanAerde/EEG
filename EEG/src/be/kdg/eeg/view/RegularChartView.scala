package be.kdg.eeg.view

import be.kdg.eeg.view.util.LineChartView
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Label
import javafx.scene.layout.{BorderPane, HBox, VBox}

/**
  * Describes the view for the RegularChart
  */
class RegularChartView extends ChartView {
  //NODES
  private  val _chart: LineChartView = {
    val xAxis = new NumberAxis(ChartView.X_AXIS_LABEL, 0, ChartView.MAX_TIME, ChartView.TICK_UNIT)
    xAxis.setAutoRanging(false)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(ChartView.Y_AXIS_LABEL)
    new LineChartView(xAxis, yAxis)
  }
  layoutNodes()

  override def layoutNodes(): Unit = {
    val bottomPane = layoutToolbar()
    this.setBottom(bottomPane)
    _chart.setTitle(ChartView.CHART_TITLE)
  }

  def layoutToolbar(): BorderPane = {
    val vboxData = new VBox(new Label(ChartView.DATA), _comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(ChartView.STIMULUS), _comboBoxStimulus)
    val vboxContact = new VBox(new Label(ChartView.CONTACT_POINT), _comboBoxContactPoint)
    val hbox = new HBox(vboxData, vboxStimulus, vboxContact, _btnPlay)
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
  def chart: LineChartView = _chart
}