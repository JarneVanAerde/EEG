package be.kdg.eeg.view

import javafx.geometry.{Insets, Pos}
import javafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.scene.control.{Button, ComboBox, Label}
import javafx.scene.layout.{BorderPane, HBox, VBox}

class StartView extends BorderPane {
  final val PADDING = new Insets(0, 20, 20, 20)
  val comboBoxStimulus = new ComboBox[String]
  val comboBoxContactPoint = new ComboBox[String]
  val comboBoxPersonInput = new ComboBox[String]
  val btnClear = new Button("Clear chart")
  val lineChart: LineChart[Number, Number] = {
    val xAxis = new NumberAxis
    xAxis.setLabel("Time")
    val yAxis = new NumberAxis
    yAxis.setForceZeroInRange(false)
    yAxis.setLabel("Activity")
    val line = new LineChart(xAxis, yAxis)
    line
  }

  layoutNodes(lineChart, comboBoxStimulus, comboBoxPersonInput, comboBoxContactPoint, btnClear)

  def layoutNodes(chart: LineChart[Number, Number], comboBoxStimulus: ComboBox[String], comboBoxPersonInput: ComboBox[String],
                  comboBoxContactPoint: ComboBox[String], btnClear: Button): Unit = {
    val vboxData = new VBox(new Label("Data:"), comboBoxPersonInput)
    val vboxStimulus = new VBox(new Label("Stimulus:"), comboBoxStimulus)
    val vboxContact = new VBox(new Label("Contact point:"), comboBoxContactPoint)
    val hbox = new HBox(vboxData, vboxStimulus, vboxContact, btnClear)
    hbox.setAlignment(Pos.BOTTOM_CENTER)
    hbox.setPadding(PADDING)
    hbox.getStyleClass.add("toolbar")
    hbox.setSpacing(10)
    chart.setCreateSymbols(false)
    chart.setTitle("EEG results for Barbara")
    this.setCenter(chart)
    this.setBottom(new BorderPane(hbox))
  }
}