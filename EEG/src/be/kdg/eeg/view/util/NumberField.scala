package be.kdg.eeg.view.util

import javafx.scene.control.TextField

/**
  * Custom TextField class that only accepts numbers with a max length of 1.
  */
class NumberField extends TextField {
  this.textProperty().addListener((_,oldValue,newValue) => {
    val allDigit = newValue forall Character.isDigit
    if (!allDigit || newValue.length > 1) {
      if (!oldValue.isEmpty) this.setText(oldValue)
      else this.clear()
    }
  })
  this.getStyleClass.add("number-field")
}
