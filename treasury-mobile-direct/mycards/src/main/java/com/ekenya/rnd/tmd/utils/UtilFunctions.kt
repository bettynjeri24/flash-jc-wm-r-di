package com.ekenya.rnd.tmd.utils

import androidx.navigation.NavOptions
import com.ekenya.rnd.mycards.R

/**
 * Return navOptions for animating navigation
 */
fun getAppearNavOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.fade_in_auth)
        .setExitAnim(R.anim.fade_out_auth)
        .setPopEnterAnim(R.anim.fade_in_auth)
        .setPopExitAnim(R.anim.fade_out_auth)
        .build()
}

/**
 * Return navOptions for animating navigation
 */
fun getSlideNavOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left_auth)
        .setExitAnim(R.anim.slide_out_left_auth)
        .setPopEnterAnim(R.anim.slide_in_right_auth)
        .setPopExitAnim(R.anim.slide_out_right_auth)
        .build()
}
