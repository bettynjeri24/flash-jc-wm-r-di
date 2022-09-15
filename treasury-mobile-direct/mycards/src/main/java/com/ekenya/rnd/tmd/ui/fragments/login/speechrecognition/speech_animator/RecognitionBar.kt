package com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition.speech_animator

import android.graphics.RectF

class RecognitionBar(var x: Int, var y: Int, height: Int, maxHeight: Int, val radius: Int) {

    var height: Int = height
    val maxHeight: Int = maxHeight
    val startX: Int = x
    val startY: Int = y
    val rect: RectF = RectF(
        (x - radius).toFloat(),
        (y - height / 2).toFloat(),
        (x + radius).toFloat(),
        (y + height / 2).toFloat()
    )

    fun update() {
        rect[
            (x - radius).toFloat(), (
                y - height / 2
                ).toFloat(), (
                x + radius
                ).toFloat()
        ] = (
            y + height / 2
            ).toFloat()
    }
}
