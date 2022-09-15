package com.ekenya.rnd.baseapp.ui.bixolon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlueToothData(
    val name: String,
    val address: String
) : Parcelable

const val TAG = "TAG"
const val REQUEST_CODE_BLUETOOTH = 1
const val REQUEST_ENABLE_BT = 101
const val REQUEST_LOCATION = 102
const val DEVICE_ADDRESS_START = " ("
const val DEVICE_ADDRESS_END = ")"
