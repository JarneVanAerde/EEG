package be.kdg.eeg.presenter

import be.kdg.eeg.model.stimulus.StimulusServiceStore
import be.kdg.eeg.view.util.Animation
import be.kdg.eeg.view.{ChartView, MenuView, RegularChartView, SlidingWindowView}

/**
  * This class handles all events that are emitted on the MenuView.
  * It updates the view accordingly.
  *
  * @param view corresponding view.
  */
final class MenuPresenter(val view: MenuView, val store: StimulusServiceStore) {
  addEventHandlers()

  def addEventHandlers(): Unit = {
    Array(
      view.btnSlidingWindow,
      view.btnRegularChart
    ).foreach(button => {
      button.setOnMouseEntered(_ => Animation.scale(button, toXY = 1.1))
      button.setOnMouseExited(_ => Animation.scale(button, fromXY = 1.1))
    })

    view.btnRegularChart.setOnAction(_ => {
      val newView = new RegularChartView()
      new RegularChartPresenter(newView, store)
      view.getScene.setRoot(newView)
    })

    view.btnSlidingWindow.setOnAction(_ => {
      val newView = new SlidingWindowView()
      new SlidingWindowPresenter(newView, store)
      view.getScene.setRoot(newView)
    })
  }
}
