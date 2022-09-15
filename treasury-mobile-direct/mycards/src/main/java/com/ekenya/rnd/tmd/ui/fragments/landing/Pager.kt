package com.ekenya.rnd.tmd.ui.fragments.landing

import com.ekenya.rnd.mycards.R

data class Pager(
    val title: String,
    val subTitle: String,
    val image: Int
)

val landingList = mutableListOf<Pager>().apply {
    add(
        Pager(
            "Welcome to Treasury Mobile Direct",
            "Invest in our treasury bills and bonds to safe Guard your future",
            R.drawable.splash_one
        )
    )
    add(
        Pager(
            "The smartest thing to do With your money",
            "Invest in our treasury bills and bonds to safe Guard your future",
            R.drawable.splash_two
        )
    )
    add(
        Pager(
            "Invest your money like an Expert",
            "Smart, easy and convenient way to do your Long-term and short-term investments",
            R.drawable.splash_three
        )
    )
}
