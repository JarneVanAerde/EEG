package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{Stimulus, StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.RegularChartView
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.scene.chart.{LineChart, XYChart}

class RegularChartPresenter(val view: RegularChartView) {

  val store = new StimulusServiceStore

  addEventHandlers()
  updateView()


  def addEventHandlers(): Unit = {
    view.btnClear.setOnAction(_ => clearChart())
    view.comboBoxStimulus.valueProperty().addListener((_, _, newValue) => {
      updateStimulus(newValue)
    })
    view.comboBoxContactPoint.valueProperty().addListener((_, _, newValue) => {
      updateContactPoint(newValue)
    })
    view.comboBoxPersonInput.valueProperty().addListener((_, _, newValue) => {
      updateView(newValue)
      clearChart()
      view.lineChart.setTitle("EEG results for " + newValue)
    })
  }

  def clearChart(): Unit = view.lineChart.getData.clear()
  def getModel: StimulusService = store.getService(view.comboBoxPersonInput.getValue)

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
    getModel.stimuli.foreach(line => {
      stimulusOptions.add(line.word)
    })
    getModel.getAllContactPointNames.foreach(point => {
      contactPointOptions.add(point)
    })
    view.comboBoxContactPoint.setItems(contactPointOptions)
    view.comboBoxStimulus.setItems(stimulusOptions)
  }

  def updateChart(title: String, yValues: Vector[Double]) = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.lineChart.getData.add(series)
  }

  def updateStimulus(newValue: String): Unit = {
    val contactPoint = view.comboBoxStimulus.getValue
    if (contactPoint != null && newValue != null) {
      getModel.getContactPointValuesForStimulus(newValue, contactPoint)
    }
  }

  def updateContactPoint(newValue: String): Unit = {
    val stimulus = view.comboBoxStimulus.getValue
    if (stimulus != null && newValue != null) {
      val data = getModel.getContactPointValuesForStimulus(stimulus, newValue)
      updateChart(stimulus + ": " + newValue, data)
    }
  }
}
