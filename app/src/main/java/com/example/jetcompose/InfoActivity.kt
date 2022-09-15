package com.example.jetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetcompose.compose.ViewMoreTvShowInfo
import com.example.jetcompose.model.TvShow
import com.example.jetcompose.utils.TVSHOWID

class InfoActivity : ComponentActivity() {
    private val tvShow: TvShow by lazy {
        intent?.getSerializableExtra(TVSHOWID) as TvShow
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewMoreTvShowInfo(tvShow = tvShow)
        }
    }
}
