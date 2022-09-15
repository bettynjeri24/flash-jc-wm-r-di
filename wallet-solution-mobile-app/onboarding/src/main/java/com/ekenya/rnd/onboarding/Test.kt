package com.ekenya.rnd.onboarding

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout


class Test(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(com.ekenya.rnd.onboarding.R.layout.test_layout, this, true)
    }
}