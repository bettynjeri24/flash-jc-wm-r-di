package com.ekenya.rnd.dashboard.utils

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.changeBackgroundColor(color:Int) {
    val mColor = ContextCompat.getColor(context,color)

    val states = arrayOf(

        intArrayOf(android.R.attr.state_enabled)
    )

    val mColors = intArrayOf(

        mColor

    )
    val mColorList = ColorStateList(states, mColors)

    backgroundTintList = mColorList

}
fun ExtendedFloatingActionButton.changeBackgroundColor(color:Int) {
    val mColor = ContextCompat.getColor(context,color)

    val states = arrayOf(

        intArrayOf(android.R.attr.state_enabled)
    )

    val mColors = intArrayOf(

        mColor

    )
    val mColorList = ColorStateList(states, mColors)

    backgroundTintList = mColorList

}