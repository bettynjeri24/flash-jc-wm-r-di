package com.ekenya.lamparam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_adds.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentAdds : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_adds, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carouselView.pageCount = 4
        val ads =
            intArrayOf(R.mipmap.onboarding1, R.mipmap.onboarding2, R.mipmap.onboarding3, R.mipmap.onboarding1)
        carouselView.setImageListener { position, imageView -> imageView.setImageResource(ads[position]) }
    }
}
