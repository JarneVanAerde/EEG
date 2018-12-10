package be.kdg.eeg.view.util

import com.sun.javafx.charts.Legend
import javafx.scene.Cursor
import javafx.scene.chart.{Axis, LineChart, XYChart}
import javafx.scene.control.Tooltip
import javafx.util.Duration

/**
  * Custom linechart with extended functionality.
  *
  * @param xAxis x axis data
  * @param yAxis y axis data
  * @tparam X X axis type
  * @tparam Y Y axis type
  */
class LineChartView[X, Y](xAxis: Axis[X], yAxis: Axis[Y]) extends LineChart[X, Y](xAxis: Axis[X], yAxis: Axis[Y]) {

  /**
    * Adds an onclick listener to the chart legend symbols.
    * When the legend is clicked the corresponding chart is hidden/shown.
    */
  def enableHideOnClick(): Unit = {
    this.getChildrenUnmodifiable.forEach {
      case l: Legend =>
        l.getItems.forEach(li => {
          this.getData.filtered(s => s.getName.equals(li.getText))
            .forEach(s => {
              li.getSymbol.setCursor(Cursor.HAND)
              li.getSymbol.setOnMouseClicked(_ => {
                s.getNode.setVisible(!s.getNode.isVisible)
                s.getData.forEach(d => d.getNode.setVisible(s.getNode.isVisible))
              })
            })
        })
      case _ =>
    }
  }

  /**
    * Adds tooltips to all datapoints on the chart
    */
  def addTooltips(showDelay: Duration): Unit = {
    this.getData.forEach(s => {
      s.getData.filtered(d => d.getNode.isVisible).forEach(d => {
        val tooltip = new Tooltip("%s\n%s: %s\n%s: %sms".format(s.getName, yAxis.getLabel,
          (math floor d.getYValue.toString.toFloat * 100) / 100, xAxis.getLabel, d.getXValue.toString))
        tooltip.setShowDelay(showDelay)
        Tooltip.install(d.getNode, tooltip)
        d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("hover-contact-point"))
        d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.remove("hover-contact-point"))
      })
    })
  }


  /**
    * Highlights and x value in the chart.
    * @param series serie of the x value
    * @param xValue x value to be highlighted
    * @param highlightWidth the width of the highlight 'beam'
    */
  def highlightData(series: XYChart.Series[Number, Number], xValue: Int, highlightWidth: Int): Unit = {
    series.getData
      .filtered(d => d.getXValue == xValue)
      .forEach(d => {
        d.getNode.setStyle("" +
          "-fx-background-color: rgba(255,255,255,.1);" +
          "-fx-pref-height: 2000px;" +
          "-fx-pref-width: " + highlightWidth + "px;")
      })
  }

  /**
    * Checks if the data that is being added is already on the chart.
    *
    * @param title of the legend
    * @return true of false wether or not the data is already added
    */
  def dataAlreadyAdded(title: String): Boolean = {
    this.getChildrenUnmodifiable.forEach {
      case l: Legend =>
        l.getItems.forEach(li => {
          if (li.getText.equals(title)) return true
        })
      case _ =>
    }
    false
  }

  /**
    * Changes the color of a data serie
    * @param series the specified series
    * @param hexCode the color in hexadecimal
    */
  def changeSeriesColor(series: XYChart.Series[Number, Number], hexCode: String): Unit = {
    this.getData.forEach(node => {
      if (node == series) {
        node.getNode.lookup(".chart-series-line").setStyle("-fx-stroke: " + hexCode + ";")
      }
    })
  }

}
