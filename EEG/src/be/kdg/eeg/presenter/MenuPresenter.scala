package be.kdg.eeg.presenter

import be.kdg.eeg.view.{MenuView, RegularChartView, SlidingWindowView}

class MenuPresenter(val view: MenuView) {
  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
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

  def updateView(): Unit = {
  }
}
