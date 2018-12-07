package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, SlidingWindowView}
import com.sun.javafx.charts.Legend
import javafx.collections.FXCollections
import javafx.scene.Cursor
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import javafx.util.Duration

class SlidingWindowPresenter(val view: SlidingWindowView, val store: StimulusServiceStore) {
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
        tooltip.setShowDelay(CHART_TOOLTIP_DELAY)
        Tooltip.install(d.getNode, tooltip)
        d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("hover-contact-point"))
        d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.remove("hover-contact-point"))
      })
    })
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
    * Checks if the data that is being added is already on the chart.
    *
    * @param title of the legend
    * @return true of false
    */
  def dataAlreadyAdded(title: String): Boolean = {
    view.getChart.getChildrenUnmodifiable.forEach {
      case l: Legend =>
        l.getItems.forEach(li => {
          if (li.getText.equals(title)) return true
        })
      case _ =>
    }
    false
  }

  /**
    * Adds and average line for a given contactpoint.
    * It will only be added if the corresponding checkbox is checked.
    *
    * @param stimulus     given word
    * @param contactPoint given point to calculate average for
    */
  def addAvgLine(stimulus: String, contactPoint: String): Unit = {
    if (view.getCheckboxAvgLine.isSelected) {
      val average = getModel.analyseTools.getAvgForContactPoints(stimulus, contactPoint)
      val title = s"avg: ${view.getComboBoxPersonInput.getValue} - $stimulus: $contactPoint"
      addDataToChart(title, Vector.fill(512)(average))
    }
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
      if (!dataAlreadyAdded(title)) {
        addDataToChart(title, data)
        addAvgLine(stimulus, contactPoint)
        enableHideOnClick()
        addTooltips()
      }
    }
  }

  def clearChart(): Unit = view.getChart.getData.clear()

  def getModel: StimulusService = store.getService(view.getComboBoxPersonInput.getValue)
}
