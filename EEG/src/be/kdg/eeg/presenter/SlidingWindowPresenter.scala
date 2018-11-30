package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{Stimulus, StimulusService}
import be.kdg.eeg.view.RegularChartView
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.scene.chart.{LineChart, XYChart}
import be.kdg.eeg.model.stimulus.StimulusService
import be.kdg.eeg.view.SlidingWindowView

class SlidingWindowPresenter(val view: SlidingWindowView) {

  val stimuliService = new StimulusService("files/Barbara_NounVerb.csv", "Barbara")

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.btnClear.setOnAction(event => {
      view.lineChart.getData.clear()
    })
    view.comboBoxStimulus.valueProperty().addListener(new ChangeListener[String] {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
        val contactPoint = view.comboBoxStimulus.getValue
        if (contactPoint != null && newValue != null) {
          stimuliService.getContactPointValuesForStimulus(newValue, contactPoint)
        }
      }
    })
    view.comboBoxContactPoint.valueProperty().addListener(new ChangeListener[String] {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
        val stimulus = view.comboBoxStimulus.getValue
        if (stimulus != null && newValue != null) {
          val data = stimuliService.getContactPointValuesForStimulus(stimulus, newValue)
          updateChart(stimulus + ": " + newValue, data)
        }
      }
    })
  }

  def updateView(): Unit = {
    val stimulusOptions = FXCollections.observableArrayList[String]
    val contactPointOptions = FXCollections.observableArrayList[String]
    val dataOptions = FXCollections.observableArrayList[String]
    dataOptions.add("Barbara")
    dataOptions.add("Bart")
    stimuliService.stimuli.foreach(line => {
      stimulusOptions.add(line.word)
    })
    stimuliService.getAllContactPointNames.foreach(point => {
      contactPointOptions.add(point)
    })
    view.comboBoxContactPoint.setItems(contactPointOptions)
    view.comboBoxStimulus.setItems(stimulusOptions)
    view.comboBoxPersonInput.setItems(dataOptions)
  }

  def updateChart(title: String, yValues: Vector[Double]) = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.lineChart.getData.add(series)
  }
}