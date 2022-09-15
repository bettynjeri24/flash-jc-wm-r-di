package io.eclectics.cargilldigital.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentPrivacynSecurityBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.printer.PrinterActivity
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.utils.NetworkUtility
import javax.inject.Inject

@AndroidEntryPoint
class PrivacynSecurity : Fragment() {
    @Inject
    lateinit var navOptions: NavOptions
    private var _binding: FragmentPrivacynSecurityBinding? = null
    private val binding get() = _binding!!
    lateinit var userData: UserDetailsObj
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPrivacynSecurityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(requireActivity())
         userData= NetworkUtility.jsonResponse(userJson)
        checkProfile()
        binding.cardChangePin.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("phone",userData.phoneNumber)
            findNavController().navigate(R.id.nav_changePin,bundle,navOptions)
        }
        binding.cardContactUs.setOnClickListener { findNavController().navigate(R.id.nav_contact_us,null,navOptions) }

        binding.cardPrinterSetting.setOnClickListener{
            var intent = Intent(requireActivity(), PrinterActivity::class.java)
            intent.putExtra("printerdata","rttrer")
            intent.putExtra("isSettings",true)
            requireActivity().startActivity(intent)
        }

        binding.tvName.text = "${userData.firstName} ${userData.lastName}"
        binding.tvAccPhoneNumber.text = userData.phoneNumber
    }

    private fun checkProfile() {
        //check if profile buyer and activate printer settings
        //Buyers
        if(userData.role.contentEquals("Buyers")){
            binding.cardPrinterSetting.visibility = View.VISIBLE
        }
        else{
            binding.cardPrinterSetting.visibility = View.GONE
        }
    }
}