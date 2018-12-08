package be.kdg.eeg.view

import javafx.scene.chart.{LineChart, NumberAxis}
import javafx.scene.control._
import javafx.scene.layout.{AnchorPane, BorderPane, HBox, VBox}
import javafx.scene.shape.Rectangle
import javafx.util.Duration

class SlidingWindowView extends BorderPane {
  private final val MAX_TIME: Double = 512
  private final val TICK_UNIT: Double = 50
  private final val DATA = "Data:"
  private final val STIMULUS = "Stimulus:"
  private final val CONTACT_POINT = "Contact point:"
  private final val X_AXIS = "Time"
  private final val Y_AXIS = "Activity"
  private final val CLEAR_CHART = "Clear"
  private final val BACK = "Back"
  private final val ADD_DATA = "Add data"
  private final val AVERAGE_LINE = "Average line"
  private final val BACK_TOOLTIP = "Back to menu"
  private final val CLEAR_TOOLTIP = "Clear the chart"
  private final val ADD_DATA_TOOLIP = "Add data to the chart"
  private final val CHART_TITLE = "Brain activity over time"
  private final val BUTTON_TOOLTIP_DELAY = new Duration(500)

  //Nodes
  private val comboBoxStimulus = new ComboBox[String]
  private val comboBoxContactPoint = new ComboBox[String]
  private val comboBoxPersonInput = new ComboBox[String]
  private val btnClear = new Button(CLEAR_CHART)
  private val btnBack = new Button(BACK)
  private val btnAddData = new Button(ADD_DATA)
  private val btnAvgLine = new Button(AVERAGE_LINE)
  private val tooltipBack = new Tooltip(BACK_TOOLTIP)
  private val tooltipClear = new Tooltip(CLEAR_TOOLTIP)
  private val tooltipAddData = new Tooltip(ADD_DATA_TOOLIP)
  private val window = new Rectangle(10, 0)
  tooltipBack.setShowDelay(BUTTON_TOOLTIP_DELAY)
  tooltipClear.setShowDelay(BUTTON_TOOLTIP_DELAY)
  tooltipAddData.setShowDelay(BUTTON_TOOLTIP_DELAY)
  private val chart: LineChart[Number, Number] = {
    val xAxis = new NumberAxis
    xAxis.setLabel(X_AXIS)
    xAxis.setAutoRanging(false)
    xAxis.setTickUnit(TICK_UNIT)
    xAxis.setUpperBound(MAX_TIME)
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel(Y_AXIS)
    val line = new LineChart(xAxis, yAxis)
    line.setTitle(CHART_TITLE)
    line.setAnimated(false)
    line
  }

  layoutNodes()

  def layoutNodes(): Unit = {
    val toolbarPane = layoutToolbar()
    val mainContainer = new BorderPane(chart)
    mainContainer.setBottom(toolbarPane)
    window.getStyleClass.add("window")
    //The (sliding) window is put on top of all the content.
    val anchorPane = new AnchorPane(mainContainer, window)
    AnchorPane.setBottomAnchor(mainContainer, 0.0)
    AnchorPane.setTopAnchor(mainContainer, 0.0)
    AnchorPane.setLeftAnchor(mainContainer, 0.0)
    AnchorPane.setRightAnchor(mainContainer, 0.0)
    this.setCenter(anchorPane)
  }

  def layoutToolbar(): BorderPane = {
    val vboxData = new VBox(new Label(DATA), comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label(STIMULUS), comboBoxStimulus)
    val vboxContact = new VBox(new Label(CONTACT_POINT), comboBoxContactPoint)
    val hBoxToolbar = new HBox(vboxData, vboxStimulus, vboxContact, btnAvgLine, btnAddData)
    val bottomLeftPane = new BorderPane()
    val bottomRightPane = new BorderPane()
    btnBack.setTooltip(tooltipBack)
    btnClear.setTooltip(tooltipClear)
    btnAddData.setTooltip(tooltipAddData)
    bottomLeftPane.setBottom(btnBack)
    bottomRightPane.setBottom(btnClear)
    bottomLeftPane.getStyleClass.add("bottom-left-toolbar")
    bottomRightPane.getStyleClass.add("bottom-right-toolbar")
    hBoxToolbar.getStyleClass.add("toolbar")
    val bottomPane = new BorderPane(hBoxToolbar)
    bottomPane.setLeft(bottomLeftPane)
    bottomPane.setRight(bottomRightPane)
    bottomPane
  }

  def getComboBoxStimulus: ComboBox[String] = comboBoxStimulus

  def getComboBoxContactPoint: ComboBox[String] = comboBoxContactPoint

  def getComboBoxPersonInput: ComboBox[String] = comboBoxPersonInput

  def getBtnClear: Button = btnClear

  def getBtnBack: Button = btnBack

  def getBtnAddData: Button = btnAddData

  def getBtnAvgLine: Button = btnAvgLine

  def getWindow: Rectangle = window

  def getChart: LineChart[Number, Number] = chart
}