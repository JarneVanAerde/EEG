package be.kdg.eeg.view

import javafx.scene.chart.{LineChart, NumberAxis}
import javafx.scene.control.{Button, ComboBox, Label}
import javafx.scene.layout.{BorderPane, HBox, VBox}
import javafx.scene.control.Tooltip
import javafx.util.Duration

class RegularChartView extends BorderPane {
  private final val DATA = "Data:"
  private final val STIMULUS = "Stimulus:"
  private final val CONTACT_POINT = "Contact point:"
  private final val X_AXIS = "Time"
  private final val Y_AXIS = "Activity"
  private final val CLEAR_CHART = "Clear"
  private final val BACK = "Back"
  private final val BACK_TOOLTIP = "Back to menu"
  private final val CLEAR_TOOLTIP = "Clear the chart"

  //Nodes
  private val comboBoxStimulus = new ComboBox[String]
  private val comboBoxContactPoint = new ComboBox[String]
  private val comboBoxPersonInput = new ComboBox[String]
  private val btnClear = new Button(CLEAR_CHART)
  private val btnBack = new Button(BACK)
  private val tooltipBack = new Tooltip(BACK_TOOLTIP)
  tooltipBack.setShowDelay(new Duration(100))
  private val tooltipClear = new Tooltip(CLEAR_TOOLTIP)
  tooltipClear.setShowDelay(new Duration(100))
  private val chart: LineChart[Number, Number] = {
    val xAxis = new NumberAxis
    xAxis.setLabel(X_AXIS)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(Y_AXIS)
    val line = new LineChart(xAxis, yAxis)
    line
  }

  layoutNodes()

  def layoutNodes(): Unit = {
    val vboxData = new VBox(new Label(DATA), comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(STIMULUS), comboBoxStimulus)
    val vboxContact = new VBox(new Label(CONTACT_POINT), comboBoxContactPoint)
    val hbox = new HBox(vboxData, vboxStimulus, vboxContact)
    hbox.getStyleClass.add("toolbar")
    this.setCenter(chart)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    btnBack.setTooltip(tooltipBack)
    btnClear.setTooltip(tooltipClear)
    bottomLeftPane.setBottom(btnBack)
    bottomRightPane.setBottom(btnClear)
    bottomLeftPane.getStyleClass.add("bottom-left-toolbar")
    bottomRightPane.getStyleClass.add("bottom-right-toolbar")
    val bottomPane = new BorderPane(hbox)
    bottomPane.setLeft(bottomLeftPane)
    bottomPane.setRight(bottomRightPane)
    this.setBottom(bottomPane)
  }

  def getComboBoxStimulus: ComboBox[String] = comboBoxStimulus

  def getComboBoxContactPoint: ComboBox[String] = comboBoxContactPoint

  def getComboBoxPersonInput: ComboBox[String] = comboBoxPersonInput

  def getBtnClear: Button = btnClear

  def getBtnBack: Button = btnBack

  def getChart: LineChart[Number, Number] = chart
}