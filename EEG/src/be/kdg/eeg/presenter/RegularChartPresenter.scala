package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, RegularChartView}
import com.sun.javafx.charts.Legend
import javafx.collections.FXCollections
import javafx.scene.Cursor
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import javafx.util.Duration

class RegularChartPresenter(val view: RegularChartView) {
  private final val CHART_TOOLTIP_DURATION = new Duration(10)
  private val store = new StimulusServiceStore

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.getBtnAddData.setOnAction(_ => updateData())
    view.getBtnClear.setOnAction(_ => clearChart())
    view.getBtnBack.setOnAction(_ => {
      val newView = new MenuView()
      new MenuPresenter(newView)
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

  /**
    * Adds an onclick listener to the chart legend.
    * When the legend is clicked the corresponding chart is hidden.
    */
  def enableHideOnClick(): Unit = {
    view.getChart.getChildrenUnmodifiable.forEach {
      case l: Legend =>
        l.getItems.forEach(li => {
          view.getChart.getData.filtered(s => s.getName.equals(li.getText))
            .forEach(s => {
              li.getSymbol.setCursor(Cursor.HAND)
              li.getSymbol.setOnMouseClicked(_ => s.getNode.setVisible(!s.getNode.isVisible))
            })
        })
      case _ =>
    }
  }

  /**
    * Adds tooltips to all datapoints in the chart
    */
  def addTooltips(): Unit = {
    view.getChart.getData.forEach(s => {
      s.getData.filtered(d => d.getNode.isVisible).forEach(d => {
        val tooltip = new Tooltip("Activity: %s\nTime: %sms".format(
          (math floor d.getYValue.floatValue() * 100) / 100, d.getXValue.toString))
        tooltip.setShowDelay(CHART_TOOLTIP_DURATION)
        Tooltip.install(d.getNode, tooltip)
        d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("hover-contact-point"))
        d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.remove("hover-contact-point"))
      })
    })
  }

  def updateChart(title: String, yValues: Vector[Double]): Unit = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.getChart.getData.add(series)
    enableHideOnClick()
    addTooltips()
  }

  def updateData(stimulus: String = view.getComboBoxStimulus.getValue,
                 contactPoint: String = view.getComboBoxContactPoint.getValue): Unit = {
    if (stimulus != null && contactPoint != null) {
      val data = getModel.getContactPointValuesForStimulus(stimulus, contactPoint)
      updateChart(view.getComboBoxPersonInput.getValue + " - " + stimulus + ": " + contactPoint, data)
    }
  }

  def clearChart(): Unit = view.getChart.getData.clear()

  def getModel: StimulusService = store.getService(view.getComboBoxPersonInput.getValue)
}
