package io.eclectics.cargilldigital.ui.generalwalletprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentGeneralWalletBinding


class GeneralWallet : Fragment() {
    private var _binding: FragmentGeneralWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var gWalletNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGeneralWalletBinding.inflate(inflater, container, false)


        return binding.root }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gWalletNavController = Navigation.findNavController(view)

        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.fragment_agent) as NavHostFragment
        gWalletNavController = nestedNavHostFragment.navController

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}