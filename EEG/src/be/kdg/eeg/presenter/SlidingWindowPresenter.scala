package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{Stimulus, StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, RegularChartView, SlidingWindowView}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.scene.chart.{LineChart, XYChart}

class SlidingWindowPresenter(val view: SlidingWindowView) {

  private val store = new StimulusServiceStore

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.getBtnClear.setOnAction(_ => clearChart())
    view.getBtnBack.setOnAction(_ => {
      val newView = new MenuView()
      new MenuPresenter(newView)
      view.getScene.setRoot(newView)
    })
    view.getComboBoxStimulus.valueProperty().addListener((_, _, newValue) => updateData(stimulus = newValue))
    view.getComboBoxContactPoint.valueProperty().addListener((_, _, newValue) => updateData(contactPoint = newValue))
    view.getComboBoxPersonInput.valueProperty().addListener((_, _, newValue) => {
      updateView(newValue)
      clearChart()
      view.getChart.setTitle(newValue + "'s brain activity over time")
    })
  }

  def updateView(name: String = null): Unit = {
    val stimulusOptions = FXCollections.observableArrayList[String]
    val contactPointOptions = FXCollections.observableArrayList[String]
    val dataOptions = FXCollections.observableArrayList[String]
    store.getFileNames.foreach(name => dataOptions.add(name))
    view.getComboBoxPersonInput.setItems(dataOptions)
    if (name == null) {
      view.getComboBoxPersonInput.setValue(dataOptions.get(0))
    } else {
      view.getComboBoxPersonInput.setValue(name)
    }
    getModel.stimuli.foreach(line => stimulusOptions.add(line.word))
    getModel.getAllContactPointNames.foreach(point => contactPointOptions.add(point))
    view.getComboBoxContactPoint.setItems(contactPointOptions)
    view.getComboBoxStimulus.setItems(stimulusOptions)
  }

  def updateChart(title: String, yValues: Vector[Double]) = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.getChart.getData.add(series)
  }

  def updateData(stimulus: String = view.getComboBoxStimulus.getValue,
                 contactPoint: String = view.getComboBoxContactPoint.getValue): Unit = {
    if (stimulus != null && contactPoint != null) {
      val data = getModel.getContactPointValuesForStimulus(stimulus, contactPoint)
      updateChart(stimulus + ": " + contactPoint, data)
    }
  }

  def clearChart(): Unit = view.getChart.getData.clear()

  def getModel: StimulusService = store.getService(view.getComboBoxPersonInput.getValue)
}