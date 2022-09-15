package com.ekenya.rnd.onboarding.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.onboarding.R


class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = requireActivity().window.decorView.systemUiVisibility // get current flag
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // add LIGHT_STATUS_BAR to flag
            requireActivity().window.decorView.systemUiVisibility = flags
            requireActivity().window.statusBarColor = Color.WHITE // optional
        }
        return inflater.inflate(R.layout.testfragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}