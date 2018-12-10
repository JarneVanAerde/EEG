package be.kdg.eeg.view.util

import javafx.scene.control.TextField

/**
  * Custom TextField class that only accepts numbers with a max length of 1.
  */
class NumberField(lowerBound: Int, upperBound: Int) extends TextField {
  this.textProperty.addListener((_,oldValue,newValue) => {
    val allDigit = newValue forall Character.isDigit
    if (!newValue.isEmpty && (!allDigit || newValue.toInt > upperBound || newValue.toInt <= lowerBound)) {
      if (!oldValue.isEmpty) this.setText(oldValue)
      else this.clear()
    }
  })
  this.getStyleClass.add("number-field")

  def value: Int = {
    this.getText().toInt
  }

  def setValue(value: Int): Unit = {
    this.setText(value.toString)
  }
}
