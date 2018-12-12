package be.kdg.eeg.view

import javafx.scene.control.{Button, ComboBox, Tooltip}
import javafx.scene.layout.BorderPane

abstract class ChartView extends BorderPane {
  protected val _comboBoxStimulus = new ComboBox[String]
  protected val _comboBoxContactPoint = new ComboBox[String]
  protected val _comboBoxPersonInput = new ComboBox[String]
  protected val _btnClear = new Button(RegularChartView.CLEAR_CHART)
  protected val _btnBack = new Button(RegularChartView.BACK)
  protected val _btnAddData = new Button(RegularChartView.ADD_DATA)
  protected val _tooltipBack = new Tooltip(RegularChartView.BACK_TOOLTIP)
  protected val _tooltipClear = new Tooltip(RegularChartView.CLEAR_TOOLTIP)
  protected val _tooltipAddData = new Tooltip(RegularChartView.ADD_DATA_TOOLIP)
  _tooltipBack.setShowDelay(RegularChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipClear.setShowDelay(RegularChartView.BUTTON_TOOLTIP_DELAY)
  _tooltipAddData.setShowDelay(RegularChartView.BUTTON_TOOLTIP_DELAY)
  _btnBack.setTooltip(_tooltipBack)
  _btnClear.setTooltip(_tooltipClear)
  _btnAddData.setTooltip(_tooltipAddData)

  def comboBoxStimulus: ComboBox[String] = _comboBoxStimulus
  def comboBoxContactPoint: ComboBox[String] = _comboBoxContactPoint
  def comboBoxPersonInput: ComboBox[String] = _comboBoxPersonInput
  def btnClear: Button = _btnClear
  def btnBack: Button = _btnBack
  def btnAddData: Button = _btnAddData
  def layoutNodes()
}
