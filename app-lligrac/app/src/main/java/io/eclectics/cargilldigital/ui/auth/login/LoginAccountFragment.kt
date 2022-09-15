package io.eclectics.cargilldigital.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentLoginAccountBinding
import io.eclectics.cargilldigital.data.model.*
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.CustomTextWatcher
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.InputValidator.isValidPIN
import io.eclectics.cargilldigital.utils.InputValidator.isValidPhoneWithCode
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class LoginAccountFragment : Fragment() {
    private var _binding: FragmentLoginAccountBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navoption: NavOptions

    @Inject
    lateinit var pdialog: SweetAlertDialog

    val genViewModel: GeneralViewModel by viewModels()

    lateinit var phoneNumber: String
    lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginAccountBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // var userJson  = UtillPreference().getUserData(activity)
        // var userData: UserDetailsObj = NetworkUtility.jsonResponse(userJson)

        // get phone number
        // phoneNumber = UtillPreference().getLoggedphoneNumber(requireActivity())
        // binding.etPhoneNumber.setText(phoneNumber)

        setUpUi()

    }

    private fun setUpUi() {
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        binding.welcomeGreetingsTextView.text =
            resources.getString(R.string.greetings_morning) // GlobalMethods().getGreetings()
        binding.customerNameTextView.text = UtilPreference().getLoggedUserName(requireActivity())
        // set observer to clear error fields
        //  setObsver()
        /* if(loggedPhoneNumber.isNotEmpty()){
             phoneNumber = loggedPhoneNumber
         }
         else{
             //force account to lookup
         }*/

        binding.etPassword.setText("1234")
        binding.etPhoneNumber.setText("2250594851583")

        binding.btnLogin.setOnClickListener {
            // findNavController().navigate(R.id.nav_selectAccount)
            if (validData()) {
                try {
                    sendLoginRequest()
                } catch (ex: Exception) {
                    LoggerHelper.loggerError("errorlog", "log ${ex.message}")
                    val i = Intent(requireActivity(), MainActivity::class.java)
                    activity!!.finish()
                    activity!!.overridePendingTransition(0, 0)
                    startActivity(i)
                    activity!!.overridePendingTransition(0, 0)
                    // NetworkUtility().transactionWarning("Error occured, try again later",requireActivity())
                }
            }
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finishAndRemoveTask()
                // NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun setObsver() {
        binding.tlPassword.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlPassword))
        binding.tlUsername.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlUsername))
    }

    private fun validData(): Boolean {
        var pinNumber = binding.etPassword.text.toString()
        var phoneNumber = binding.etPhoneNumber.text.toString()
        if (!isValidPhoneWithCode(phoneNumber)) {
            binding.etPhoneNumber.requestFocus()
            binding.tlUsername.error = resources.getString(R.string.enter_phoneNo)
            return false
        }

        if (!isValidPIN(pinNumber)) {
            binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        return true
    }

    private fun sendLoginRequest() {
        val endpoint = ApiEndpointObj.userLogin
        val lookupJson = JSONObject()

        // lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("pin", binding.etPassword.text.toString())
        lookupJson.put("phonenumber", binding.etPhoneNumber.text.toString()) // phoneNumber
        // TODO MUST DELETE AFTER 15/04
        UtilPreference().setUserCreds(requireActivity(), binding.etPassword.text.toString())

        lifecycleScope.launch {
            pdialog.show()
            genViewModel.sendLoginRequest(lookupJson, endpoint, requireActivity())
                .observe(
                    requireActivity(),
                    Observer {
                        pdialog.dismiss()
                        when (it) {
                            is ViewModelWrapper.error -> GlobalMethods().transactionWarning(
                                requireActivity(),
                                "${it.error}"
                            ) // LoggerHelper.loggerError("error","error")
                            is ViewModelWrapper.response -> processRequest(it.value) // LoggerHelper.loggerSuccess("success","success ${it.value}")
                            // processRequest(it.value)//
                        }
                    }
                )
        }
    }


    private fun processRequest(response: String) {
        try {
// nav_selectAccount
            LoggerHelper.loggerError("offlineproduct", "response test loop")

            LoggerHelper.loggerSuccess("response", "product response $response")
            lateinit var loginResponse: String
            loginResponse = response
            var profileStr = JSONObject(response).getString("role")
            var dummySection = Section(1, "dummy", "dummy", "dummy")
            var json = NetworkUtility.getJsonParser().toJson(dummySection)
            var dummyBank = CoopBank(1, "pkau", "oak", "oak", 1)
            var bankJson = NetworkUtility.getJsonParser().toJson(dummyBank)
            lateinit var userLoginDataObj: UserLogginData
            var lookupresponse: UserDetailsObj
            when (profileStr) {
                "Employee" -> {
                    // hii json haitabui jsonnullable so try reseting it
                    var jsonResponse = JSONObject(response)
                    jsonResponse.put("section", JSONObject(json.toString()))
                    jsonResponse.put("bankAccount", JSONObject())
                    // var lookupresponse:OtherUserObj = NetworkUtility.jsonResponse(response)
                    loginResponse = jsonResponse.toString()
                    lookupresponse = NetworkUtility.jsonResponse(jsonResponse.toString())
                }
                "Cooperative" -> {
                    var jsonResponse = JSONObject(response)
                    jsonResponse.put("section", JSONObject(json.toString()))
                    // jsonResponse.put("bankAccount",JSONObject(bankJson.toString()))
                    // jsonResponse.put("bankAccount",JSONObject())
                    // var lookupresponse:OtherUserObj = NetworkUtility.jsonResponse(response)
                    loginResponse = jsonResponse.toString()
                    lookupresponse = NetworkUtility.jsonResponse(jsonResponse.toString())
                }
                else -> {
                    var jsonResponse = JSONObject(response)
                    // jsonResponse.put("bankAccount",bankJson)
                    jsonResponse.put("bankAccount", JSONObject(bankJson.toString()))
                    loginResponse = jsonResponse.toString()
                    lookupresponse =
                        NetworkUtility.jsonResponse(jsonResponse.toString()) // jsonResponse.toString()
                }
            }
            userLoginDataObj = UserLogginData(
                lookupresponse.firstName!!,
                lookupresponse.lastName!!,
                lookupresponse.userId!!,
                lookupresponse.userIndex!!,
                lookupresponse.cooperativeIndex!!,
                lookupresponse.role!!,
                lookupresponse.providedUserId!!,
                lookupresponse.cooperativeId!!,
                lookupresponse.emailAddress,
                lookupresponse.phoneNumber!!,
                "${lookupresponse.bankAccount}",
                "${lookupresponse.section}",
                lookupresponse.region,
                lookupresponse.walletBalance,
                lookupresponse.transactions.toString()
            )
            // save to room database
            UtilPreference().saveWalletBalance(requireActivity(), lookupresponse.walletBalance)
            // CONVERTERS ZIMEKATAA KUFANYA KAZI NIKAFANYA MANUALLY

            var userLoginDataJson = NetworkUtility.getJsonParser().toJson(userLoginDataObj)
            // LoggerHelper.loggerError("phonenumber","response phone ${lookupresponse.phonenumber}")
            // var lookupResponse = NetworkUtility.jsonResponse(productJson)
            // var bundle = Bundle()
            // bundle.putString("phone",requireArguments().getString("phone"))
            // save to preferences
             lifecycleScope.launch {
                // userLoginDataObj
                genViewModel.insertLoginData(requireActivity(), userLoginDataJson)
            }
            UtilPreference().setUserData(requireActivity(), loginResponse)
            // findNavController().navigate(R.id.nav_selectAccount)
            // SET DUMMEY FIRST TIME LOGIN
            UtilPreference().setLoggedUserName(
                requireActivity(),
                "${userLoginDataObj.firstName} ${userLoginDataObj.lastName}"
            )
            UtilPreference().setTirstTimeLogin(requireActivity(), false)
            UtilPreference().setCurentProfile(requireActivity(), profileStr)
            redirectToProfileAccount(profileStr)
        } catch (ex: Exception) {
            LoggerHelper.loggerError("errorlo", " ${ex.message}")
            val i = Intent(requireActivity(), MainActivity::class.java)
            activity!!.finish()
            activity!!.overridePendingTransition(0, 0)
            startActivity(i)
            activity!!.overridePendingTransition(0, 0)
            // NetworkUtility().transactionWarning("Error occurred try again later",requireActivity())
        }
    }

    private fun redirectToProfileAccount(role: String) {
        when (role) {
            "Buyers" -> {
                findNavController().navigate(R.id.nav_agentProfile, null, navoption)
            }
            "Farmer" -> {
                var bundle = Bundle()
                bundle.putString("menu", "mainmenu")
                findNavController().navigate(R.id.nav_farmerDashboard, bundle, navoption)
            }
            "Cooperative" -> {
                findNavController().navigate(R.id.nav_cooperativeProfile, null, navoption)
            }
            /* //Manager
             "Manager" ->{
                 findNavController().navigate(R.id.nav_cooperativeProfile,null, navoption)
             }*/
            "Employee" -> {
                UtilPreference().saveFloatBalance(requireActivity(), "50000")
                findNavController().navigate(R.id.nav_generalWalletProfile, null, navoption)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }
}
