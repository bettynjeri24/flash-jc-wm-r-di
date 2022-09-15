package com.ekenya.rnd.onboarding.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.onboarding.R

class ScanIdFragment : BaseDaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_id_fragment, container, false)
    }


}