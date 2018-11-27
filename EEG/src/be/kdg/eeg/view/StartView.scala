package be.kdg.eeg.views

import javafx.geometry.{Insets, Pos}
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.scene.control.{Button, ComboBox, Label}
import javafx.scene.layout.{BorderPane, HBox, VBox}

class StartView extends BorderPane {

  final val PADDING = new Insets(10, 10, 10, 10)
  val comboBoxStimulus = new ComboBox[String]
  val comboBoxContactPoint = new ComboBox[String]
  val btnClear = new Button("Clear")
  val lineChart: LineChart[Number, Number] = {
    val xAxis = new NumberAxis
    xAxis.setLabel("Time")
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel("Activity")
    val line = new LineChart(xAxis, yAxis)
    line
  }

  layoutNodes(lineChart, comboBoxStimulus, comboBoxContactPoint, btnClear)

  def layoutNodes(chart: LineChart[Number, Number], comboBoxStimulus: ComboBox[String],
                  comboBoxContactPoint: ComboBox[String], btnClear: Button): Unit = {
    val vbox1 = new VBox(new Label("Stimulus:"), comboBoxStimulus)
    val vbox2 = new VBox(new Label("Contact point:"), comboBoxContactPoint)
    val hbox = new HBox(vbox1, vbox2, btnClear)
    hbox.setAlignment(Pos.CENTER)
    val comboboxPane = new BorderPane(hbox)
    vbox1.setPadding(PADDING)
    vbox2.setPadding(PADDING)
    chart.setCreateSymbols(false)
    this.setCenter(chart)
    this.setBottom(comboboxPane)
  }
}