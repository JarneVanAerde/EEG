package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, SlidingWindowView}
import javafx.animation.{KeyFrame, Timeline}
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import javafx.scene.layout.AnchorPane
import javafx.util.Duration

class SlidingWindowPresenter(val view: SlidingWindowView, val store: StimulusServiceStore) {
  private final val CHART_TOOLTIP_DELAY = new Duration(10)
  private final val DEFAULT_SLIDING_WINDOW_SIZE = 3

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.btnAddData.setOnAction(_ => updateChart())
    view.btnClear.setOnAction(_ => clearChart())
    view.btnAvgLine.setOnAction(_ => addAvgLine())
    view.fldWindowSize.textProperty.addListener((_, _, newValue) => {
      if (!newValue.isEmpty) {
        view.window.setWidth(newValue.toDouble * 2)
      }
    })
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
    view.fldWindowSize.setValue(DEFAULT_SLIDING_WINDOW_SIZE)
  }

  /**
    * Slides the sliding window across the screen.
    * The coordinates are derived from the position of last added data, and the relative position of the chart.
    *
    * @param xValue last added xValue, decides the position of the window
    */
  def animateWindow(xValue: Double): Unit = {
    //TODO: Add new window if animation is running.
    val chartArea = view.chart.lookup(".chart-plot-background")
    val chartAreaBounds = chartArea.localToScene(chartArea.getBoundsInLocal)
    view.window.setHeight(chartAreaBounds.getMaxY - chartAreaBounds.getMinY)
    AnchorPane.setTopAnchor(view.window, chartAreaBounds.getMinY)
    AnchorPane.setLeftAnchor(view.window, view.chart.getXAxis.getDisplayPosition(xValue - 1)
      + chartAreaBounds.getMinX - view.window.getWidth)
  }

  def getWindowSize: Int = {
    if (view.fldWindowSize.getText.isEmpty) {
      view.fldWindowSize.setValue(DEFAULT_SLIDING_WINDOW_SIZE)
      return DEFAULT_SLIDING_WINDOW_SIZE
    }
    view.fldWindowSize.value
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
    view.chart.getData.add(series)
    view.window.setVisible(true)
    val xValues = getModel.getTimeFrames()
    val interestingData = getModel.analyseTools.getInterestingData(view.comboBoxStimulus.getValue,
      view.comboBoxContactPoint.getValue, slidingWindowSize = getWindowSize)
    val frame = new KeyFrame(Duration.millis(1000 / 100), _ => {
      val i = series.getData.size()
      series.getData.add(new XYChart.Data(xValues(i), yValues(i)))
      animateWindow(xValues(i))
      if (interestingData.contains(xValues(i))) {
        view.chart.highlightData(series, xValues(i))
      }
    })
    val animation = new Timeline(frame)
    animation.setCycleCount(yValues.length)
    animation.setOnFinished(_ => {
      view.window.setVisible(false)
      view.chart.enableHideOnClick()
      view.chart.addTooltips(CHART_TOOLTIP_DELAY)
    })
    animation.play()
  }

  /**
    * Adds data to the chart
    *
    * @param title   title of data (in legend)
    * @param yValues y values of the data
    * @return the added data series
    */
  def addDataToChart(title: String, yValues: Vector[Double]): XYChart.Series[Number, Number] = {
    val series = new XYChart.Series[Number, Number]
    series.setName(title)
    val xValues = getModel.getTimeFrames()
    yValues.indices.foreach(i => {
      series.getData.add(new XYChart.Data(xValues(i), yValues(i)))
    })
    view.chart.getData.add(series)
    series
  }

  /**
    * Adds and average line for a given contactpoint.
    * It will only be added if the corresponding checkbox is checked.
    */
  def addAvgLine(): Unit = {
    val stimulus: String = view.comboBoxStimulus.getValue
    val contactPoint: String = view.comboBoxContactPoint.getValue
    if (stimulus != null && contactPoint != null) {
      val average = getModel.analyseTools.getAvgForContactPoints(stimulus, contactPoint)
      val title = s"avg: ${view.comboBoxPersonInput.getValue} - $stimulus: $contactPoint"
      if (!view.chart.dataAlreadyAdded(title)) {
        val series = addDataToChart(title, Vector.fill(512)(average))
        view.chart.enableHideOnClick()
        view.chart.addTooltips(CHART_TOOLTIP_DELAY)
        view.chart.changeSeriesColor(series, "#818181")
      }
    }
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
        addDataToChartWithAnimation(title, data)
      }
    }
  }

  def clearChart(): Unit = view.chart.getData.clear()

  def getModel: StimulusService = store.getService(view.comboBoxPersonInput.getValue)
}
