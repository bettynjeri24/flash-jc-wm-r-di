package com.example.jetcompose.utils

import android.content.Context
import android.content.Intent
import com.example.jetcompose.InfoActivity
import com.example.jetcompose.model.TvShow

const val TVSHOWID = "tvshowID"
fun intenToViewDetails(context: Context, tvShow: TvShow) =
    Intent(context, InfoActivity::class.java).apply {
        putExtra(TVSHOWID, tvShow)
    }
