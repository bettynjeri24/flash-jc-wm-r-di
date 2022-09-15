package io.eclectics.cargilldigital.ui.generalwalletprofile.statement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentFullStatementBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.CustomTextWatcher2
import io.eclectics.cargilldigital.utils.InputValidator.isValidDate
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.GlobalMethods.navigation.spinnerDatePicker
import io.eclectics.cargill.utils.NetworkUtility
import org.joda.time.DateTime
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class FullStatement : Fragment() {
    private var _binding: FragmentFullStatementBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navoption: NavOptions
    lateinit var userData: UserDetailsObj
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFullStatementBinding.inflate(inflater, container, false)
       return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etEndDate.addTextChangedListener(CustomTextWatcher2(binding.etEndDate))
        binding.etStartDate.addTextChangedListener(CustomTextWatcher2(binding.etStartDate))
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)

        binding.etemaiilAddress.setText(userData.emailAddress)
        binding.btnBuyAirtime.setOnClickListener {
            sendRequest()
        }

        binding.etStartDate.setOnClickListener {
            val endDateStr = binding.etEndDate.text.toString()
            val startDateStr = binding.etStartDate.text.toString()
            val endDate = if (isValidDate(endDateStr)) {
                GlobalMethods().createDate(endDateStr)
            } else {
                DateTime.now()
            }

            val currentDate = if (isValidDate(startDateStr)) {
                GlobalMethods().createDate(startDateStr)
            } else {
                DateTime.now()
            }
            spinnerDatePicker(
                binding.etStartDate,
                currentDate,
                getString(R.string.start_date_hint),
                maxDate = endDate
            )
        }
        binding.etEndDate.setOnClickListener {
            val endDateStr = binding.etEndDate.text.toString()

            val currentDate = if (isValidDate(endDateStr)) {
                GlobalMethods().createDate(endDateStr)
            } else {
                DateTime.now()
            }
            spinnerDatePicker(
                binding.etEndDate,
                currentDate,
                getString(R.string.end_date),
                null,
                DateTime.now()
            )
        }
    }

    private fun sendRequest() {
        var email = binding.etemaiilAddress.text.toString()
        var endpoint  =  ApiEndpointObj.fullStatementRequest
        var lookupJson = JSONObject()
        lookupJson.put("phonenumber",userData.phoneNumber)
        lookupJson.put("email",email)
        lookupJson.put("startdate",binding.etStartDate.text.toString())
        lookupJson.put("enddate",binding.etEndDate.text.toString())
        lookupJson.put("userIndex",userData.userIndex)
        lookupJson.put("endPoint",endpoint)
        var bundle = Bundle()
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_pinFragment,bundle,navoption)
    }
}