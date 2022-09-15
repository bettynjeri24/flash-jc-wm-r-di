package io.eclectics.cargilldigital.ui.cooperativeprofile.coopbuyers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.eclectics.cargilldigital.R


class BuyerLastTransactions : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_last_transactions, container, false)
    }


}