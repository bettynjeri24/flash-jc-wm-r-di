package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentSampleFarmforceQrcodeBinding
import io.eclectics.cargilldigital.qrcode.QRCodeGenerator
import io.eclectics.cargilldigital.utils.ToolBarMgmt

@AndroidEntryPoint
class SampleFarmforceQRCODE : Fragment() {
    private var _binding: FragmentSampleFarmforceQrcodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSampleFarmforceQrcodeBinding.inflate(inflater, container, false)
        ToolBarMgmt.setPayfarmerToolbarTitle(resources.getString(R.string.pay_farmer),resources.getString(R.string.pay_or_scan),binding.mainLayoutToolbar,requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileQRCode()
    }
    private fun setProfileQRCode() {
        try{

                var phoneNumber = "2250701686379"
                var transID = "TXN454522545224"
                var date = "Vendredi 29 Avril"
            var amount = "25000"
            var time = "12:20"
                var userIndex = "12154512"
                var name = "Marc Zogoury"
                var stringToGenerate = "$transID~$date~$amount~$time~$name~$phoneNumber"
            //var stringToGenerate = "$userId~$userIndex~$phoneNumber~$coopId~$name"
                var qrBitmap = QRCodeGenerator.generateQr(stringToGenerate)
                binding.profileQRCode.setImageBitmap(qrBitmap)

                // var section = userData.getSection()
                //binding.tvSection.text = section.sectionName
                //binding.tvUserLocation.text = userData.
                //depending on the profile show user account details


        }catch (ex:Exception){}
    }
}