package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.{StimulusService, StimulusServiceStore}
import be.kdg.eeg.view.{MenuView, SlidingWindowView}
import javafx.animation.Animation.Status
import javafx.animation.{KeyFrame, Timeline}
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.util.Duration

class SlidingWindowPresenter(val view: SlidingWindowView, val store: StimulusServiceStore) {
  private final val CHART_TOOLTIP_DELAY = new Duration(10)
  private final val DEFAULT_SLIDING_WINDOW_SIZE = 3
  private final val DEFAULT_SLIDING_WINDOW_SPEED = 95
  private final val HIGHLIGHT_COLOR = Color.rgb(130, 176, 191, 0.1)
  //animation of the sliding window
  private val animation = new Timeline()

  def onStop(): Unit = {
    animation.stop()
    view.window.setVisible(false)
    view.chart.enableHideOnClick()
    view.chart.addTooltips(CHART_TOOLTIP_DELAY)
    animation.getKeyFrames.clear()
    view.btnPlay.setText("Play")
    disableNodes(false)
  }

  animation.setOnFinished(_ => {
    onStop()
  })

  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.btnPlay.setOnAction(_ => {
      if (view.btnPlay.getText.equals("Play")) {
        animation.play()
        updateChart()
        if (animation.getStatus == Status.RUNNING) view.btnPlay.setText("Pause")
      } else if (view.btnPlay.getText.equals("Pause")) {
        view.btnPlay.setText("Play")
        view.chart.enableHideOnClick()
        view.chart.addTooltips(CHART_TOOLTIP_DELAY)
        animation.pause()
      }
    })
    view.btnClear.setOnAction(_ => clearChart())
    view.btnAvgLine.setOnAction(_ => addAvgLine())
    view.fldWindowSize.textProperty.addListener((_, _, newValue) => {
      if (!newValue.isEmpty) {
        view.window.setWidth(newValue.toDouble * 2)
      }
    })
    view.fldWindowSpeed.textProperty().addListener((_, _, newValue) => {

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
    val caclTechniqueOptions = FXCollections.observableArrayList[String]("Average", "Standard deviation")
    store.getFileNames.foreach(name => dataOptions.add(name))
    view.comboBoxPersonInput.setItems(dataOptions)
    if (name == null) {
      view.comboBoxPersonInput.setValue(dataOptions.get(0))
    } else {
      view.comboBoxPersonInput.setValue(name)
    }
    view.comboBoxCalcTechnique.setValue(caclTechniqueOptions.get(0))
    view.fldMinRange.setValue(0)
    view.fldMaxRange.setValue(4)
    getModel.stimuli.foreach(line => stimulusOptions.add(line.word))
    getModel.getAllContactPointNames.foreach(point => contactPointOptions.add(point))
    view.comboBoxContactPoint.setItems(contactPointOptions)
    view.comboBoxStimulus.setItems(stimulusOptions)
    view.comboBoxCalcTechnique.setItems(caclTechniqueOptions)
    view.fldWindowSize.setValue(DEFAULT_SLIDING_WINDOW_SIZE)
    view.fldWindowSpeed.setValue(DEFAULT_SLIDING_WINDOW_SPEED)
  }

  /**
    * Slides the sliding window across the screen.
    * The coordinates are derived from the position of last added data, and the relative position of the chart.
    *
    * @param xValue last added xValue, decides the position of the window
    */
  def animateWindow(xValue: Double): Unit = {
    val chartArea = view.chart.lookup(".chart-plot-background");
    val chartAreaBounds = chartArea.localToScene(chartArea.getBoundsInLocal)
    view.window.setHeight(chartAreaBounds.getMaxY - chartAreaBounds.getMinY)
    AnchorPane.setTopAnchor(view.window, chartAreaBounds.getMinY)
    AnchorPane.setLeftAnchor(view.window, view.chart.getXAxis.getDisplayPosition(xValue - 1)
      + chartAreaBounds.getMinX - view.window.getWidth)
    view.window.setVisible(true)
  }

  /**
    * @return the window size in the field, if null returns the default size.
    */
  def getWindowSize: Int = {
    if (view.fldWindowSize.getText.isEmpty) {
      view.fldWindowSize.setValue(DEFAULT_SLIDING_WINDOW_SIZE)
      return DEFAULT_SLIDING_WINDOW_SIZE
    }
    view.fldWindowSize.value
  }

  /**
    * @return the speed of the field, if null returns the default size.
    */
  def getWindowSpeed: Int = {
    if (view.fldWindowSpeed.getText.isEmpty) {
      view.fldWindowSpeed.setValue(DEFAULT_SLIDING_WINDOW_SPEED)
      return DEFAULT_SLIDING_WINDOW_SPEED
    }
    view.fldWindowSpeed.value
  }

  /**
    * Each key frame a new value is added from both the xValues and yValues.
    * The size of the data is used as the index.
    *
    * @param series      Series where the data should be added
    * @param xValues     xValues to be added
    * @param yValues     yValues to be added
    * @param xHighlights xValues to be highlighted.
    * @return
    */
  def getWindowKeyFrame(series: XYChart.Series[Number, Number],
                        xValues: Vector[Double],
                        yValues: Vector[Double],
                        xHighlights: Vector[Double]): KeyFrame = {
    new KeyFrame(Duration.millis(51 - getWindowSpeed / 2), _ => {
      val i = series.getData.size()
      val data = new XYChart.Data[Number, Number](xValues(i), yValues(i))
      series.getData.add(data)
      animateWindow(xValues(i))
      if (i != 0 && i != xValues.length - 1 && xHighlights.contains(xValues(i))) {
        val prev = xHighlights contains xValues(i - 1)
        val next = xHighlights contains xValues(i + 1)
        if (!prev && next) {
          view.chart.newHighlight(data, HIGHLIGHT_COLOR, series.getName)
        }
        if (prev && next) {
          view.chart.extendHighlight(data)
        }
      }
    })
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
    val xValues = getModel.getTimeFrames()
    val minRange = view.fldMinRange.value
    val maxRange = view.fldMaxRange.value

    val interestingData = getModel.analyseTools.getInterestingData(view.comboBoxStimulus.getValue,
      view.comboBoxContactPoint.getValue, slidingWindowSize = getWindowSize,
      useAvg = if (view.comboBoxCalcTechnique.getValue.equalsIgnoreCase("average")) true else false,
      minRange = if (minRange >= maxRange) 0 else minRange,
      maxRange = if (maxRange <= minRange) 4 else maxRange)

    animation.getKeyFrames.addAll(getWindowKeyFrame(series, xValues, yValues, interestingData))
    animation.setCycleCount(yValues.length)
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
        disableNodes(true)
      }
    }
  }

  def disableNodes(bool: Boolean): Unit = {
    view.comboBoxStimulus.setDisable(bool)
    view.comboBoxPersonInput.setDisable(bool)
    view.comboBoxContactPoint.setDisable(bool)
    view.fldWindowSize.setDisable(bool)
    view.btnAvgLine.setDisable(bool)
    view.fldWindowSpeed.setDisable(bool)
  }

  def clearChart(): Unit = {
    if (view.chart.getData.size() > 0) {
      view.chart.getData.remove(0, view.chart.getData.size())
    }
    onStop()
    view.chart.clearHighlights()
  }

  def getModel: StimulusService = store.getService(view.comboBoxPersonInput.getValue)
}
