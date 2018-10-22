package be.kdg.eeg.views

import javafx.scene.chart.{LineChart, NumberAxis}
import javafx.scene.layout.{BorderPane, VBox}

class StartView extends BorderPane {

  val lineChart: LineChart[Number, Number] = {
    val xAxis = new NumberAxis
    xAxis.setLabel("No of employees")
    val yAxis = new NumberAxis
    yAxis.setLabel("Revenue per employee")
    new LineChart(xAxis,yAxis)
  }

  layoutNodes(lineChart)

  def layoutNodes(arg1: LineChart[Number, Number]): Unit = {
    val vbox = new VBox(arg1)
    this.setCenter(vbox)
  }
}
