package com.ekenya.rnd.common.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.ekenya.rnd.common.abstractions.BaseBottomSheetDialogFragment
import java.lang.IllegalArgumentException

abstract class BaseCommonCargillBottomSheetDialogFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : BaseBottomSheetDialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) throw IllegalArgumentException("Binding Not Found")

        return binding.root
    }

}

