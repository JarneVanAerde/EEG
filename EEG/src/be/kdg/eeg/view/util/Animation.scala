package be.kdg.eeg.view.util

import javafx.animation.ScaleTransition
import javafx.scene.control.Button
import javafx.util.Duration

object Animation {
  def scale(button: Button, fromXY: Double = 1, toXY: Double = 1): Unit = {
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
