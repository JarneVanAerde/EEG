package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, ChartView}
import com.sun.javafx.charts.Legend
import javafx.collections.FXCollections
import javafx.scene.Cursor
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import javafx.util.Duration

class RegularChartPresenter(val view: ChartView, val store: StimulusServiceStore) {
  private final val CHART_TOOLTIP_DELAY = new Duration(10)

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.btnAddData.setOnAction(_ => updateChart())
    view.btnClear.setOnAction(_ => clearChart())
    view.btnBack.setOnAction(_ => {
      val newView = new MenuView()
      new MenuPresenter(newView, store)
      view.getScene.setRoot(newView)
    })
    view.comboBoxPersonInput.valueProperty().addListener((_, _, newValue) => updateView(newValue))
  }

  def updateView(name: String = null): Unit = {
    val stimulusOptions = FXCollections.observableArrayList[String]
    val contactPointOptions = FXCollections.observableArrayList[String]
    val dataOptions = FXCollections.observableArrayList[String]
    store.getFileNames.foreach(name => dataOptions.add(name))
    view.comboBoxPersonInput.setItems(dataOptions)
    if (name == null) {
      view.comboBoxPersonInput.setValue(dataOptions.get(0))
    } else {
      view.comboBoxPersonInput.setValue(name)
    }
    getModel.stimuli.foreach(line => stimulusOptions.add(line.word))
    getModel.getAllContactPointNames.foreach(point => contactPointOptions.add(point))
    view.comboBoxContactPoint.setItems(contactPointOptions)
    view.comboBoxStimulus.setItems(stimulusOptions)
  }

  def addDataToChart(title: String, yValues: Vector[Double]): Unit = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.chart.getData.add(series)
  }

  /**
    * Updates the chart if both a stimulus and a contactpoint is present
    */
  def updateChart(): Unit = {
    val stimulus: String = view.comboBoxStimulus.getValue
    val contactPoint: String = view.comboBoxContactPoint.getValue
    if (stimulus != null && contactPoint != null) {
      val data = getModel.getContactPointValuesForStimulus(stimulus, contactPoint)
      val title = s"${view.comboBoxPersonInput.getValue} - $stimulus: $contactPoint"
      if (!view.chart.dataAlreadyAdded(title)) {
        addDataToChart(title, data)
        view.chart.enableHideOnClick()
        view.chart.addTooltips(CHART_TOOLTIP_DELAY)
      }
    }
  }

  def clearChart(): Unit = view.chart.getData.clear()

  def getModel: StimulusService = store.getService(view.comboBoxPersonInput.getValue)
}
