package com.ekenya.rnd.dashboard.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.removeActionBarElevation() {
    supportActionBar!!.elevation = 0F;
}