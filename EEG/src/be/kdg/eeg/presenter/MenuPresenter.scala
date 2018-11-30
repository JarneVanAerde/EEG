package be.kdg.eeg.presenter

import be.kdg.eeg.view.{MenuView, StartView}

class MenuPresenter(val view: MenuView) {
  addEventHandlers()
  updateView()

  def addEventHandlers(): Unit = {
    view.btnRegularChart.setOnAction(event => {
      val startView = new StartView()
      new StartPresenter(startView)
      view.getScene.setRoot(startView)
    })
  }

  def updateView(): Unit = {
  }
}
