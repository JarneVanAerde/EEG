package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, RegularChartView}
import com.sun.javafx.charts.Legend
import javafx.collections.FXCollections
import javafx.scene.Cursor
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import javafx.util.Duration

class RegularChartPresenter(val view: RegularChartView, val store: StimulusServiceStore) {
  private final val CHART_TOOLTIP_DELAY = new Duration(10)

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.getBtnAddData.setOnAction(_ => updateChart())
    view.getBtnClear.setOnAction(_ => clearChart())
    view.getBtnBack.setOnAction(_ => {
      val newView = new MenuView()
      new MenuPresenter(newView, store)
      view.getScene.setRoot(newView)
    })
    view.getComboBoxPersonInput.valueProperty().addListener((_, _, newValue) => updateView(newValue))
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

  def addDataToChart(title: String, yValues: Vector[Double]): Unit = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.getChart.getData.add(series)
  }

  /**
    * Updates the chart if both a stimulus and a contactpoint is present
    */
  def updateChart(): Unit = {
    val stimulus: String = view.getComboBoxStimulus.getValue
    val contactPoint: String = view.getComboBoxContactPoint.getValue
    if (stimulus != null && contactPoint != null) {
      val data = getModel.getContactPointValuesForStimulus(stimulus, contactPoint)
      val title = s"${view.getComboBoxPersonInput.getValue} - $stimulus: $contactPoint"
      if (!view.getChart.dataAlreadyAdded(title)) {
        addDataToChart(title, data)
        view.getChart.enableHideOnClick()
        view.getChart.addTooltips(CHART_TOOLTIP_DELAY)
      }
    }
  }

  def clearChart(): Unit = view.getChart.getData.clear()

  def getModel: StimulusService = store.getService(view.getComboBoxPersonInput.getValue)
}
