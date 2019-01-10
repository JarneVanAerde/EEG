package be.kdg.eeg.view.util

import com.sun.javafx.charts.Legend
import javafx.scene.Cursor
import javafx.scene.chart.XYChart.Data
import javafx.scene.chart.{Axis, LineChart, XYChart}
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Custom linechart with extended functionality.
  *
  * @param xAxis x axis data
  * @param yAxis y axis data
  * @tparam X X axis type
  * @tparam Y Y axis type
  */
class LineChartView(xAxis: Axis[Number], yAxis: Axis[Number]) extends LineChart(xAxis: Axis[Number], yAxis: Axis[Number]) {
  val highlights = new mutable.HashMap[String, mutable.ArrayBuffer[Rectangle]]()

  /**
    * Adds an onclick listener to the chart legend symbols.
    * When the legend is clicked the corresponding chart is hidden/shown.
    */
  def enableHideOnClick(): Unit = {
    this.getChildrenUnmodifiable.forEach {
      case l: Legend =>
        l.getItems.forEach(li => {
          getData.filtered(s => s.getName.equals(li.getText))
            .forEach(s => {
              li.getSymbol.setCursor(Cursor.HAND)
              li.getSymbol.setOnMouseClicked(_ => {
                s.getNode.setVisible(!s.getNode.isVisible)
                s.getData.forEach(d => d.getNode.setVisible(s.getNode.isVisible))
                //hide highlights:
                highlights(s.getName).foreach(rect => rect.setVisible(!rect.isVisible))
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
    getData.forEach(s => {
      s.getData.filtered(d => d.getNode.isVisible).forEach(d => {
        val tooltip = new Tooltip("%s\n%s: %s\n%s: %s".format(s.getName, yAxis.getLabel,
          (math floor d.getYValue.toString.toFloat * 100) / 100, xAxis.getLabel,
          (math floor d.getXValue.toString.toFloat * 100) / 100))
        tooltip.setShowDelay(showDelay)
        Tooltip.install(d.getNode, tooltip)
        d.getNode.setOnMouseEntered(_ => d.getNode.getStyleClass.add("hover-contact-point"))
        d.getNode.setOnMouseExited(_ => d.getNode.getStyleClass.remove("hover-contact-point"))
      })
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
    *
    * @param series  the specified series
    * @param hexCode the color in hexadecimal
    */
  def changeSeriesColor(series: XYChart.Series[Number, Number], hexCode: String): Unit = {
    getData.forEach(node => {
      if (node == series) {
        node.getNode.lookup(".chart-series-line").setStyle("-fx-stroke: " + hexCode + ";")
      }
    })
  }

  def clearHighlights(): Unit = {
    highlights.clear()
    getPlotChildren.remove(0, getPlotChildren.size())
  }

  def newHighlight(marker: Data[Number, Number], color: Color, name: String): Unit = {
    val rectangle = new Rectangle(0, 0, 0, 0)
    rectangle.setFill(color)
    rectangle.setX(getXAxis.getDisplayPosition(marker.getXValue) + 0.5) // 0.5 for crispness
    rectangle.setY(0d)
    rectangle.setHeight(getBoundsInLocal.getHeight)
    getPlotChildren.add(rectangle)
    if (!highlights.keySet.contains(name)) {
      highlights.put(name, new ArrayBuffer[Rectangle]())
    }
    highlights(name) += rectangle
  }

  def extendHighlight(marker: Data[Number, Number]): Unit = {
    val mark = getXAxis.getDisplayPosition(marker.getXValue)
    val x = highlights.values.last.last.getX
    highlights.values.last.last.setWidth(mark - x)
    highlights.values.flatten.foreach(rect => rect.toBack())
  }
}
