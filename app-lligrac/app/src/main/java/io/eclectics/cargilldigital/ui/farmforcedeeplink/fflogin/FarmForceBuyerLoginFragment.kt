package io.eclectics.cargilldigital.ui.farmforcedeeplink.fflogin

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.dk.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.databinding.FragmentLoginAccountBinding
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.FarmForceCargillViewModel
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.MEDIA_TYPE_JSON
import io.eclectics.cargilldigital.utils.InputValidator.isValidPIN
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.dk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class FarmForceBuyerLoginFragment : BaseCommonCargillFragment<FragmentLoginAccountBinding>(
    FragmentLoginAccountBinding::inflate
) {

    private var pin: String? = null
    private val loginRequestJson = JSONObject()

    @Inject
    lateinit var viewModel: FarmForceCargillViewModel
    private var onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // activity!!.finishAndRemoveTask()
                // NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
                errorTransactionCanceled(
                    messageToShow = getString(R.string.cancel_transaction)
                )
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("FarmForceBuyerLoginFragment VIEWMODEL.UNIXTIMESTAMP == ${FFUNIXTIMESTAMP}")
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        setUpIu()
        checkPermissions()
        //binding.etPhoneNumber.setText("2250703035850")
        //binding.etPassword.setText("1234")
    }

    private fun setUpIu() {

        addLeadingZeroesToNumber("20")
        Timber.e("SIM_CARD_NAMES === \n$SIMCARDNAMES")
        Timber.e("TRIM_PHONE_NUMBER === \n${trimPhoneNumber(binding.etPhoneNumber.text.toString())}")

        getDataFromFFApplication()
        fetchApiResponse()
        binding.welcomeGreetingsTextView.text =
            resources.getString(R.string.greetings_morning)
        binding.customerNameTextView.text = UtilPreference().getLoggedUserName(requireActivity())

        binding.btnLogin.setOnClickListener {
            if (validData()) {
                nologinJustCheckingIfFieldAreOkay()

//                if (isConnectionAvailable()) {
//                    initiateLogin()
//                } else {
//                    useUssdToSendDataWhenNoInternet()
//                }
            }
        }
    }

    private fun nologinJustCheckingIfFieldAreOkay() {
        if (binding.etPassword.text.toString().trim() == "1234") {
            lifecycleScope.launch {
                sweetAlertDialog!!.show()
                delay(200)
                sweetAlertDialog!!.dismiss()
                findNavController().navigate(
                    R.id.farmForceBuyerFragment
                )
            }
        } else {
            binding.etPassword.error = resources.getString(R.string.enter_secure_pin)
        }


    }


    private fun getDataFromFFApplication() {
        val data: Uri? = requireActivity().intent?.data
        // Figure out what to do based on the intent type
        if (data != null) {
            val parameter: List<String> = data.pathSegments
            val parameter2 = data.query
            val params = parameter[parameter.size - 1]
            Timber.e("${data.getQueryParameter("buyerPhonenumber")}")
            binding.etPhoneNumber.setText("${data.getQueryParameter("buyerPhonenumber")}")

            Timber.e(
                "FARM FORCE PASS KEY IS =========\n ${
                    data.getQueryParameter("paymentKey")?.replace(" ", "+")
                }\n\n"
            )

        } else {
            binding.btnLogin.isEnabled = false
        }
    }


    private fun validData(): Boolean {
        val pinNumber = binding.etPassword.text.toString()
        /*val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        if (phoneNumber.isEmpty() && phoneNumber.length != 13) {
            binding.tlUsername.error = resources.getString(R.string.enter_phoneNo)
            return false
        }*/

        if (!isValidPIN(pinNumber)) {
            binding.tlPassword.error = resources.getString(R.string.enter_secure_pin)
            return false
        }
        return true
    }

    private fun initiateLogin() {
        val pinNumber = binding.etPassword.text.toString()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        pin = pinNumber
        loginRequestJson.put("deviceId", requireActivity().getDeviceId())
        loginRequestJson.put("pin", pin)
        loginRequestJson.put(
            "phonenumber",
            phoneNumber
        )
        PIN = pin!!
        viewModel.loginUser(
            loginRequestJson.toString().toRequestBody(MEDIA_TYPE_JSON)
        )

        sweetAlertDialog!!.show()
    }

    private fun fetchApiResponse() {
        /**
         * Logging Observer
         */
        viewModel.getloggedInUser.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ResourceNetwork.Success -> {
                        sweetAlertDialog!!.dismiss()
                        if (it.value.statusCode == 0) {
                            Timber.e("ACCESS_TOKEN: ${it.value.loginResponseData!!.token}")

                            // Constants.SITE_ID = 3
                            AUTH_TOKEN = it.value.loginResponseData.token!!
                            PHONENUMBER = it.value.loginResponseData.phoneNumber!!
                            CARGILL_USER_ID = it.value.loginResponseData.userId!!
                            CARGILL_USER_NAME =
                                "${it.value.loginResponseData.firstName!!} ${it.value.loginResponseData.lastName!!}"
                            Timber.e("Email is :${it.value.loginResponseData.role}")

                            redirectToProfileAccount(it.value.loginResponseData.role.toString())
                        } else {
                            errorNetworkConnectionFailed(
                                messageToShow = it.value.statusDescription.toString()
                            )
                            Timber.e("******************registered==1** ${it.value.statusDescription}")
                        }
                    }
                    is ResourceNetwork.Failure -> {
                        handleApiErrorAndRouteToUssd(
                            failure = it,
                            handleNetworkFailure = {
                                useUssdToSendDataWhenNoInternet()
                            },
                            handleOtherErrors = {
                                sweetAlertDialog!!.dismiss()
                                // errorNetworkConnectionFailed()
                                errorBuyerAccountDoesntExist(getString(R.string.error_farmer_account_doesnt_exist))
                            }
                        )

                        Timber.d(" handleApiError  $it")
                    }
                    else -> {
                        Timber.e("ELSE SOMETHING ELSE HAS ISSUES")
                    }
                }
            }
        )

        /**
         * OBSERVE OFFLINE MODE USSD RESPONSE MESSAGE
         */
        //qwertyuiopasdfghjklzxcvbnmqwerty
        viewModel.responseMessage.observe(viewLifecycleOwner, Observer {
            sweetAlertDialog!!.dismiss()
            Timber.e("OFFLINE MODE $it")
            if (it != null) {
                try {
                    if (it.startsWith("{")) {
                        val response = Gson().fromJson(it, JsonObject::class.java)
                        Timber.e("OFFLINE MODE USSD RESPONSE $it\n\n")
                        Timber.e("response ${response.get("response")}")

                        Timber.e("response ${response.get("responseCode").asInt.toString()}")

                        if (response.get("responseCode").asInt.toString() == "0") {
                            findNavController().navigate(
                                R.id.farmForceBuyerFragment
                            )
                        } else if (response.get("responseCode").asInt.toString() == "1") {
                            errorBuyerAccountDoesntExist(messageToShow = response.get("response").asString)
                        }
                    } else if (it == "-1") {
                        errorNetworkConnectionFailed()
                    } else if (it.contains("votre", ignoreCase = true)) {
                        errorNetworkConnectionFailed(messageToShow = it)
                    } else {
                        errorNetworkConnectionFailed()
                    }
                } catch (e: JSONException) {
                    Timber.e("JSONException ${e.message}")
                }

            } else {
                Timber.e("RESPONSE IS EMPTY")
            }

        })
    }

    private fun useUssdToSendDataWhenNoInternet() {
        viewModel.offlineUssdModeBackGround(
            ApiEndpointObj.USSDLOGIN.toString() +
                    trimPhoneNumber(binding.etPhoneNumber.text.toString()) +
                    binding.etPassword.text.toString()
        )
    }

    fun redirectToProfileAccount(role: String) {
        when (role) {
            "Buyers" -> {
                findNavController().navigate(
                    R.id.farmForceBuyerFragment
                )
            }
            "Farmer" -> {
                requireActivity().showCargillCustomWarningDialog(
                )
            }
            "Cooperative" -> {
                requireActivity().showCargillCustomWarningDialog(

                )
            }
            "Employee" -> {
                requireActivity().showCargillCustomWarningDialog(
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}