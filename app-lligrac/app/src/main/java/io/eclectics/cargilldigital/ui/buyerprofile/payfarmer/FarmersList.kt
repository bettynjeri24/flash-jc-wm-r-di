package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.viewmodel.BuyerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FarmersList : Fragment() {
    @Inject
    lateinit var navOptions: NavOptions
    val buyerViewModel: BuyerViewModel by viewModels()
    lateinit var userData: UserDetailsObj
    @Inject
    lateinit var pdialog: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farmers_list, container, false)
    }


}