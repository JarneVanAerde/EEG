package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, SlidingWindowView}
import javafx.animation.{KeyFrame, Timeline}
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import javafx.scene.layout.AnchorPane
import javafx.util.Duration

class SlidingWindowPresenter(val view: SlidingWindowView, val store: StimulusServiceStore) {
  private final val CHART_TOOLTIP_DELAY = new Duration(10)

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.getBtnAddData.setOnAction(_ => updateChart())
    view.getBtnClear.setOnAction(_ => clearChart())
    view.getBtnAvgLine.setOnAction(_ => addAvgLine())
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
    * Slides the sliding window across the screen.
    * The coordinates are derived from the position of last added data, and the relative position of the chart.
    * @param seriesSize the length of the series, indicates the position of last added data
    */
  def animateWindow(seriesSize: Int): Unit = {
    //TODO: Add new window if animation is running.
    val chartArea = view.getChart.lookup(".chart-plot-background")
    val chartAreaBounds = chartArea.localToScene(chartArea.getBoundsInLocal)
    AnchorPane.setTopAnchor(view.getWindow, chartAreaBounds.getMinY)
    view.getWindow.setHeight(chartAreaBounds.getMaxY-chartAreaBounds.getMinY)
    AnchorPane.setLeftAnchor(view.getWindow, view.getChart.getXAxis.getDisplayPosition(seriesSize - 1)
      + chartAreaBounds.getMinX - view.getWindow.getWidth)
  }

  /**
    * Adds data to the chart with animation
    *
    * @param title   title of data (in legend)
    * @param yValues y values of the data
    */
  def addDataToChartWithAnimation(title: String, yValues: Vector[Double]): Unit = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    view.getChart.getData.add(series)
    view.getWindow.setVisible(true)
    val frame = new KeyFrame(Duration.millis(1000/200), _ => {
      val i = series.getData.size()
      series.getData.add(new XYChart.Data(i, yValues(i)))
      animateWindow(i)
    })
    val animation = new Timeline(frame)
    animation.setCycleCount(yValues.length)
    animation.setOnFinished(_ => {
      view.getWindow.setVisible(false)
      view.getChart.enableHideOnClick()
      view.getChart.addTooltips(CHART_TOOLTIP_DELAY)
    })
    animation.play()
  }

  /**
    * Adds data to the chart
    *
    * @param title   title of data (in legend)
    * @param yValues y values of the data
    */
  def addDataToChart(title: String, yValues: Vector[Double]): Unit = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(i, yValues(i)))
    })
    view.getChart.getData.add(series)
  }

  /**
    * Adds and average line for a given contactpoint.
    * It will only be added if the corresponding checkbox is checked.
    */
  def addAvgLine(): Unit = {
    val stimulus: String = view.getComboBoxStimulus.getValue
    val contactPoint: String = view.getComboBoxContactPoint.getValue
    if (stimulus != null && contactPoint != null) {
      val average = getModel.analyseTools.getAvgForContactPoints(stimulus, contactPoint)
      val title = s"avg: ${view.getComboBoxPersonInput.getValue} - $stimulus: $contactPoint"
      if (!view.getChart.dataAlreadyAdded(title)) {
        addDataToChart(title, Vector.fill(512)(average))
        view.getChart.enableHideOnClick()
        view.getChart.addTooltips(CHART_TOOLTIP_DELAY)
      }
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
      if (!view.getChart.dataAlreadyAdded(title)) {
        addDataToChartWithAnimation(title, data)
      }
    }
  }

  def clearChart(): Unit = view.getChart.getData.clear()

  def getModel: StimulusService = store.getService(view.getComboBoxPersonInput.getValue)
}
