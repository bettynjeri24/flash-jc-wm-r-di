package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.DurationAccountAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ServiceProviderAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.VehicleAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.ZoneAdapter
import com.ekenya.rnd.tijara.databinding.FragmentDailyParkingLookBinding
import com.ekenya.rnd.tijara.requestDTO.ParkingLookUpDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber
import java.util.*

class DailyParkingLookFragment : Fragment() {
    private lateinit var binding:FragmentDailyParkingLookBinding
    private lateinit var viewmodel:DailyParkingViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDailyParkingLookBinding.inflate(layoutInflater)
        viewmodel=ViewModelProvider(this).get(DailyParkingViewmodel::class.java)
        binding.lifecycleOwner=this
        bindUserPhotoSized(binding.ivLogo, Constants.BILLERURL!!)
        binding.progressr.visibility=View.VISIBLE
        binding.progressr.tv_pbTitle.text=getString(R.string.we_are_fetching_parking)
        binding.progressr.tv_pbTex.text=getString(R.string.please_wait)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.vehicleTypes.observe(viewLifecycleOwner, Observer {
            val adapter= VehicleAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.vehicleSpinner.adapter=adapter
            binding.vehicleSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.VEHICLEID= it[position].iD.toString()
                    Constants.VEHICLENAME= it[position].name
                    Timber.d("SPROVIDERNAME is:  ${Constants.SPROVIDERNAME}")
                }
            }
        })
        viewmodel.parkingZone.observe(viewLifecycleOwner, Observer {
            val adapter= ZoneAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.zoneSpinner.adapter=adapter
            binding.zoneSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.ZONEID= it[position].iD.toString()
                    Constants.ZONENAME= it[position].name
                }
            }
        })
        if (Constants.BILLERCODE=="NRBC_DAILY_PARKING"){
            binding.durationSp.visibility=View.GONE
            binding.tvDuration.visibility=View.GONE
        }else{
            binding.durationSp.visibility=View.VISIBLE
            binding.tvDuration.visibility=View.VISIBLE
        }
        viewmodel.duration.observe(viewLifecycleOwner, Observer {
            val adapter= DurationAccountAdapter(requireContext(),it!!)
            adapter.notifyDataSetChanged()
            binding.durationSp.adapter=adapter
            binding.durationSp.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Constants.DURATIONID= it[position].iD
                    Constants.DURATIONNAME= it[position].description
                }
            }
        })
        binding.apply {
            btnAccLookUP.setOnClickListener {
                val numberPlate=etAccNo.text.toString()
                if (numberPlate.isEmpty()){
                    tlAccNo.error=getString(R.string.please_enter_your_number)
                }else if (Constants.VEHICLENAME.toLowerCase(Locale.ROOT).isEmpty()){
                    tlAccNo.error=""
                showToast(requireContext(),"Select parking charge")
                }else if (Constants.ZONENAME.toLowerCase(Locale.ROOT).isEmpty()){
                showToast(requireContext(),"Select parking zone")
                }else{
                    tlAccNo.error=""
                    if (Constants.BILLERCODE=="NRBC_DAILY_PARKING") {
                        val parkingLookUpDTO = ParkingLookUpDTO()
                        parkingLookUpDTO.accountNumber = numberPlate
                        parkingLookUpDTO.billerCode = Constants.BILLERCODE
                        parkingLookUpDTO.chargeId = Constants.VEHICLEID
                        parkingLookUpDTO.zoneCodeId = Constants.ZONEID
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        viewmodel.parkingLookup(parkingLookUpDTO)
                    }else{
                        val parkingLookUpDTO = ParkingLookUpDTO()
                        parkingLookUpDTO.accountNumber = numberPlate
                        parkingLookUpDTO.billerCode = Constants.BILLERCODE
                        parkingLookUpDTO.chargeId = Constants.VEHICLEID
                        parkingLookUpDTO.zoneCodeId = Constants.ZONEID
                        parkingLookUpDTO.durationId=Constants.DURATIONID
                        binding.progressr.visibility = View.VISIBLE
                        binding.progressr.tv_pbTitle.visibility = View.GONE
                        binding.progressr.tv_pbTex.text = getString(R.string.please_wait)
                        viewmodel.parkingLookup(parkingLookUpDTO)
                    }

                }
            }
        }
        setupNavUp()
        viewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        findNavController().navigate(R.id.action_dailyParkingLookFragment_to_countPayBillFragment)
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(),viewmodel.statusMessage.value)
                    }
                    else -> {
                        binding.progressr.visibility=View.GONE
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))

                    }
                }
            }
        })
        viewmodel.statusCodeParking.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.progressr.visibility=View.GONE
                when (it) {

                    1 -> {
                        binding.progressr.visibility=View.GONE
                        viewmodel.stopObserving()
                    }
                    0 -> {
                        waringAlertDialogUp(requireContext(),requireView(),getString(R.string.oops_we_are_sorry)
                            ,getString(R.string.unable_to_complete_your_request))
                        binding.progressr.visibility=View.GONE
                        viewmodel.stopObserving()
                    }
                    else -> {
                        binding.progressr.visibility=View.GONE
                 }
                }
            }
        })
    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = camelCase(Constants.BILLER_NAME)
    }

}