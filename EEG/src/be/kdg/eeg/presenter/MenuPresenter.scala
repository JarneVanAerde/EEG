package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.StimulusServiceStore
import be.kdg.eeg.view.{MenuView, RegularChartView, SlidingWindowView}
import javafx.animation.ScaleTransition
import javafx.scene.control.Button
import javafx.util.Duration

/**
  * This class handles all events that are emitted on the MenuView.
  * It updates the view accordingly.
  * @param view corresponding view
  */
class MenuPresenter(val view: MenuView, val store: StimulusServiceStore) {

  addEventHandlers()

  def addEventHandlers(): Unit = {
    Array(
      view.getBtnSlidingWindow,
      view.getBtnRegularChart
    ).foreach(button => {
      button.setOnMouseEntered(_ => scale(button, toXY = 1.1))
      button.setOnMouseExited(_ => scale(button, fromXY = 1.1))
    })
    view.getBtnRegularChart.setOnAction(_ => {
      val newView = new RegularChartView()
      new RegularChartPresenter(newView, store)
      view.getScene.setRoot(newView)
    })
    view.getBtnSlidingWindow.setOnAction(_ => {
      val newView = new SlidingWindowView()
      new SlidingWindowPresenter(newView, store)
      view.getScene.setRoot(newView)
    })
  }

  private def scale(button: Button, fromXY: Double = 1, toXY: Double = 1): Unit = {
    val st = new ScaleTransition()
    st.setNode(button)
    st.setFromX(fromXY)
    st.setFromY(fromXY)
    st.setToX(toXY)
    st.setToY(toXY)
    st.setDuration(Duration.millis(50))
    st.play()
  }
}
