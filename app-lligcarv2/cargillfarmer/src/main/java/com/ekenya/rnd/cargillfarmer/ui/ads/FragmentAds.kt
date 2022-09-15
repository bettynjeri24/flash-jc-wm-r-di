package com.ekenya.rnd.cargillfarmer.ui.ads

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ekenya.rnd.cargillfarmer.databinding.AdsRowViewWalletLayoutBinding
import com.synnapps.carouselview.ImageListener

class FragmentAds : Fragment() {
    private lateinit var binding: AdsRowViewWalletLayoutBinding
    var images = intArrayOf(
        com.ekenya.rnd.common.R.drawable.my_account_1_wallet,
        com.ekenya.rnd.common.R.drawable.my_account_wallet,
        com.ekenya.rnd.common.R.drawable.my_account_1_wallet,
        com.ekenya.rnd.common.R.drawable.my_account_wallet
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdsRowViewWalletLayoutBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.carouselView.pageCount = images.size
        binding.carouselView.setImageListener(ImageListener { position: Int, imageView: ImageView ->
            imageView.setImageResource(
                images[position]
            )
        })
    }
}