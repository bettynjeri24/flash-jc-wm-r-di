package com.ekenya.lamparam

object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun testURL(): String
}