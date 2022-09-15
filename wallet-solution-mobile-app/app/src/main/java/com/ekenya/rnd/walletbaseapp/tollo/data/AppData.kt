package com.ekenya.rnd.walletbaseapp.tollo.data

import com.ekenya.rnd.walletbaseapp.R
import com.ekenya.rnd.walletbaseapp.tollo.models.SliderImage

import javax.inject.Inject

class AppData @Inject constructor() {



    public fun getImages(): MutableList<SliderImage> {
        val images= ArrayList<SliderImage>()
        images.add(SliderImage(R.color.black))
        images.add(SliderImage(R.color.purple_700))
        images.add(SliderImage(R.color.teal_200))
        return  images

    }








}