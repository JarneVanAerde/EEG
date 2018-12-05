package be.kdg.eeg.presenter

import be.kdg.eeg.view.{MenuView, RegularChartView, SlidingWindowView}
import javafx.animation.ScaleTransition
import javafx.scene.control.Button
import javafx.util.Duration

class MenuPresenter(val view: MenuView) {
  addEventHandlers()

  def addEventHandlers(): Unit = {
    Array(
      view.btnSlidingWindow,
      view.btnRegularChart
    ).foreach(button => {
      button.setOnMouseEntered((event) => scale(button, 1, 1.1))
      button.setOnMouseExited((event) => scale(button, 1.1, 1))
    })
    view.btnRegularChart.setOnAction(event => {
      val newView = new RegularChartView()
      new RegularChartPresenter(newView)
      view.getScene.setRoot(newView)
    })
    view.btnSlidingWindow.setOnAction(event => {
      val newView = new SlidingWindowView()
      new SlidingWindowPresenter(newView)
      view.getScene.setRoot(newView)
    })
  }

  private def scale(button: Button, fromXY: Double, toXY: Double): Unit = {
    val st = new ScaleTransition()
    st.setNode(button)
    st.setFromX(fromXY)
    st.setFromY(fromXY)
    st.setToX(toXY)
    st.setToY(toXY)
    st.setDuration(Duration.millis(50))
    st.play
  }
}
