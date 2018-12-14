package be.kdg.eeg.view

import javafx.scene.control.{Button, ComboBox, Tooltip}
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.util.Duration

abstract class ChartView extends BorderPane {
  protected val _comboBoxStimulus = new ComboBox[String]
  protected val _comboBoxContactPoint = new ComboBox[String]
  protected val _comboBoxPersonInput = new ComboBox[String]
  protected val _btnClear = new Button(ChartView.CLEAR_CHART)
  protected val _btnBack = new Button()
  protected val _btnPlay = new Button(ChartView.ADD_DATA)
  protected val _tooltipBack = new Tooltip(ChartView.BACK_TOOLTIP)
  protected val _tooltipClear = new Tooltip(ChartView.CLEAR_TOOLTIP)
  protected val _tooltipAddData = new Tooltip(ChartView.ADD_DATA_TOOLIP)
  private val _imvHome = new ImageView(ChartView.IMG_BACK)
  _tooltipBack.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipClear.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipAddData.setShowDelay(ChartView.BUTTON_TOOLTIP_DELAY)
  _btnBack.setTooltip(_tooltipBack)
  _btnClear.setTooltip(_tooltipClear)
  _btnPlay.setTooltip(_tooltipAddData)
  _btnPlay.getStyleClass.add("button-play")
  _btnClear.getStyleClass.add("button-stop")
  _imvHome.setFitHeight(25)
  _imvHome.setFitWidth(25)
  _btnBack.setGraphic(_imvHome)

  def layoutNodes()

  //GETTERS
  def comboBoxStimulus: ComboBox[String] = _comboBoxStimulus
  def comboBoxContactPoint: ComboBox[String] = _comboBoxContactPoint
  def comboBoxPersonInput: ComboBox[String] = _comboBoxPersonInput
  def btnClear: Button = _btnClear
  def btnBack: Button = _btnBack
  def btnPlay: Button = _btnPlay
  def getAllComboBoxes: Seq[ComboBox[String]] = Seq(_comboBoxContactPoint, _comboBoxPersonInput, _comboBoxStimulus)
}

object ChartView {
  //CONSTANTS
  val MAX_TIME: Double = 4000
  val TICK_UNIT: Double = 500
  val DATA = "Data:"
  val STIMULUS = "Stimulus:"
  val CONTACT_POINT = "Data point:"
  val X_AXIS_LABEL = "Time (ms)"
  val Y_AXIS_LABEL = "Activity"
  val CLEAR_CHART = "Clear"
  val IMG_BACK = "be/kdg/eeg/view/img/home.png"
  val ADD_DATA = "Play"
  val BACK_TOOLTIP = "Back to menu"
  val CLEAR_TOOLTIP = "Clear the chart"
  val ADD_DATA_TOOLIP = "Add data to the chart"
  val CHART_TITLE = "Brain activity over time"
  val BUTTON_TOOLTIP_DELAY = new Duration(500)
}
