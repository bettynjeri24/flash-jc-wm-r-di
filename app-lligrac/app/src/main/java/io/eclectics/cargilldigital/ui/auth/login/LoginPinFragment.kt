package io.eclectics.cargilldigital.ui.auth.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.AppCargillDigital.Companion.deviceSessionUUID
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.databinding.FragmentLoginPinBinding
import io.eclectics.cargilldigital.network.ApiEndpointObj.USSDLOGIN
import io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel.MEDIA_TYPE_JSON
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.dk.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginPinFragment : BaseCommonCargillFragment<FragmentLoginPinBinding>(
    FragmentLoginPinBinding::inflate
) {
    @Inject
    lateinit var viewModel: AuthViewModel

    private var pin: String? = null
    private lateinit var customKeyboard: MyCustomKeyboard
    private var fourDigitPin = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        AUTH_TOKEN = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        setUpUi()
    }

    private fun setUpUi() {
        fetchApiResponse()
        setCustomKeyboard()

        binding.welcomeGreetingsTextView.text = requireContext().greetingsDependingOnTimeOfDay()
        binding.customerNameTextView.text =
            UtilPreference().getLoggedUserName(requireActivity())

        binding.tvMobileNo.text = UtilPreference().getLoggedPhoneNumber(requireActivity())

        binding.forgotPass.setOnClickListener {
            requireActivity().showCargillCustomWarningDialog(
                description = getString(R.string.reset_pin_warning_message)
            )
        }
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
                        if (it.value.statusCode == 0) {
                            dismissCustomDialog()
                            timber("ACCESS_TOKEN: ${it.value.loginResponseData!!.token}")

                            // Constants.SITE_ID = 3
                            AUTH_TOKEN = it.value.loginResponseData.token!!
                            CURRENT_USER_PHONENUMBER = it.value.loginResponseData.phoneNumber!!
                            CARGILL_USER_ID = it.value.loginResponseData.userId!!
                            CARGILL_USERINDEX = it.value.loginResponseData.userIndex.toString()
                            CARGILL_PROVIDEDUSERID = it.value.loginResponseData.providedUserId!!
                            CARGILL_COOPERATIVEID = it.value.loginResponseData.cooperativeId!!
                            CARGILL_COOPERATIVEINDEX = it.value.loginResponseData.cooperativeIndex!!
                            CARGILL_SECTION = it.value.loginResponseData.section?.id.toString()
                            CARGILL_FULL_NAME =
                                "${it.value.loginResponseData.firstName!!} ${it.value.loginResponseData.lastName!!}"
                            CARGILL_USER_NAME =
                                "${it.value.loginResponseData.firstName!!} ${it.value.loginResponseData.lastName!!}"
                            Timber.e("Email is :${it.value.loginResponseData.role}")

                            UtilPreference().setLoggedUserName(
                                requireActivity(),
                                "${it.value.loginResponseData.firstName} ${it.value.loginResponseData.lastName}"
                            )
                            UtilPreference().setTirstTimeLogin(requireActivity(), false)
                            UtilPreference().setCurentProfile(
                                requireActivity(),
                                it.value.loginResponseData.role.toString()
                            )
                            lifecycleScope.launch {
                                viewModel.saveUserVmRoom(it.value.loginResponseData)
                            }

                            redirectToProfileAccount(it.value.loginResponseData.role.toString())
                        } else {
                            dismissCustomDialog()
                            // snackBarCustom("Please contact the Support team to Register you")
                            snackBarCustom(it.value.statusDescription)
                            timber("******************registered==1** ${it.value.statusDescription}")
                        }
                    }
                    is ResourceNetwork.Failure -> {
                        // dismissCustomDialog()
                        pin =
                            binding.codeOne.text.toString() +
                                    binding.codeTwo.text.toString() +
                                    binding.codeThree.text.toString() +
                                    binding.codeFour.text.toString()

                        handleApiErrorAndRouteToUssd(
                            failure = it,
                            handleNetworkFailure = {
                                viewModel.offlineUssdModeBackGround(
                                    USSDLOGIN.toString() +
                                            trimPhoneNumber(
                                                UtilPreference().getLoggedPhoneNumber(
                                                    requireActivity()
                                                )
                                            ) +
                                            pin
                                )
                            },
                            handleOtherErrors = {
                                dismissCustomDialog()
                                errorNetworkConnectionFailed()
                            }
                        )
                        Timber.e(" handleApiError  $it")
                    }
                    else -> {}
                }
            }
        )

        /**
         * OBSERVE OFFLINE MODE USSD RESPONSE MESSAGE
         */
        viewModel.responseMessage.observe(
            viewLifecycleOwner,
            Observer {
                dismissCustomDialog()
                Timber.e("OFFLINE MODE $it")
                if (it != null) {
                    try {
                        if (it.startsWith("{")) {
                            val response = Gson().fromJson(it, JsonObject::class.java)
                            Timber.e("OFFLINE MODE USSD RESPONSE $it\n\n")
                            Timber.e("response ${response.get("response")}")

                            if (response.get("responseCode").asInt == 0) {
                                redirectToProfileAccount("Buyers")
                            }
                        } else if (it == "-1") {
                            errorNetworkConnectionFailed()
                        } else {
                            errorNetworkConnectionFailed()
                            // snackBarCustom("Request not successful try again later!!")
                        }
                    } catch (e: JSONException) {
                        Timber.e("JSONException ${e.message}")
                    }
                } else {
                    Timber.e("RESPONSE IS EMPTY")
                }
            }
        )
    }

    private fun initiateLogin() {
        val loginRequestJson = JSONObject()
        pin =
            binding.codeOne.text.toString() +
                    binding.codeTwo.text.toString() +
                    binding.codeThree.text.toString() +
                    binding.codeFour.text.toString()

        loginRequestJson.put("deviceId", requireActivity().getDeviceId())
        loginRequestJson.put("pin", pin)
        loginRequestJson.put(
            "phonenumber",
            UtilPreference().getLoggedPhoneNumber(requireActivity()) // "2250703035850"
        ) // phoneNumber
        SECRET_KEY = pin!!
        showCustomDialog(getString(R.string.sending_request_cargill))
        viewModel.loginUser(
            loginRequestJson.toString().toRequestBody(MEDIA_TYPE_JSON)
        )
    }


    private fun initiateLoginTest() {
        val loginRequestJson = JSONObject()
        loginRequestJson.put("deviceId", deviceSessionUUID())
        loginRequestJson.put("pin", "1002")
        loginRequestJson.put("phonenumber", "2250701686379") // phoneNumber
        SECRET_KEY = "1002"

        showCustomDialog(getString(R.string.sending_request_cargill))
        viewModel.loginUser(
            loginRequestJson.toString().toRequestBody(MEDIA_TYPE_JSON)
        )
    }

    private fun redirectToProfileAccount(role: String) {
        when (role) {
            "Buyers" -> {
                //showFeatureModule(moduleCargillBuyer)
                toast("Buyers")
            }
            "Farmer" -> {
                toast("Farmer")
                //showFeatureModule(moduleCargillFarmer)
            }
            "Cooperative" -> {
                toast(getString(R.string.cooperative_label))
            }
            "Employee" -> {
                toast("Employee")
                //showFeatureModule(moduleCargillFarmer)
            }
        }
    }

    private fun setCustomKeyboard() {
        customKeyboard = binding.keyboard
        customKeyboard.setPinView(true)

        asteriskPasswordMask()

        fourDigitPin.clear()
        binding.codeOne.requestFocus()
        customKeyboard.setInputConnection(binding.codeOne.onCreateInputConnection(EditorInfo()))
        pinValueSet()
        pinValueDeleted()
        customKeyboard.checkBtnClicked.observe(viewLifecycleOwner) {
            if (it) {
                pin =
                    binding.codeOne.text.toString() +
                            binding.codeTwo.text.toString() +
                            binding.codeThree.text.toString() +
                            binding.codeFour.text.toString()
                customKeyboard.loginUser(pin!!)
                if (customKeyboard.loginPinLengthOkay.value == true) {
                    initiateLogin()
                    customKeyboard.checkBtnClicked.value = false
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the input fields",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun pinValueSet() {
        customKeyboard.pinValue.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                if (fourDigitPin.size == 0) {
                    fourDigitPin.add(it)
                    binding.codeOne.setText(fourDigitPin[0])
                } else if (fourDigitPin.size == 1) {
                    fourDigitPin.add(it)
                    binding.codeOne.setText(fourDigitPin[0])
                    binding.codeTwo.setText(fourDigitPin[1])
                } else if (fourDigitPin.size == 2) {
                    fourDigitPin.add(it)
                    binding.codeOne.setText(fourDigitPin[0])
                    binding.codeTwo.setText(fourDigitPin[1])
                    binding.codeThree.setText(fourDigitPin[2])
                } else if (fourDigitPin.size == 3) {
                    fourDigitPin.add(it)
                    binding.codeOne.setText(fourDigitPin[0])
                    binding.codeTwo.setText(fourDigitPin[1])
                    binding.codeThree.setText(fourDigitPin[2])
                    binding.codeFour.setText(fourDigitPin[3])
                } else if (fourDigitPin.size == 4) {
                    fourDigitPin[3] = it
                    binding.codeOne.setText(fourDigitPin[0])
                    binding.codeTwo.setText(fourDigitPin[1])
                    binding.codeThree.setText(fourDigitPin[2])
                    binding.codeFour.setText(fourDigitPin[3])
                }
                customKeyboard.pinValue.value = ""
            }
        }
    }

    private fun pinValueDeleted() {
        customKeyboard.deletePinValue.observe(viewLifecycleOwner) {
            if (it) {
                if (fourDigitPin.size == 1) {
                    fourDigitPin.removeLast()
                    binding.codeOne.setText("")
                } else if (fourDigitPin.size == 2) {
                    fourDigitPin.removeLast()
                    binding.codeTwo.setText("")
                } else if (fourDigitPin.size == 3) {
                    fourDigitPin.removeLast()
                    binding.codeThree.setText("")
                } else if (fourDigitPin.size == 4) {
                    fourDigitPin.removeLast()
                    binding.codeFour.setText("")
                }
                customKeyboard.deletePinValue.value = false
            }
        }
    }

    private fun asteriskPasswordMask() {
        binding.codeOne.transformationMethod = AsteriskPasswordTransformationMethod()
        binding.codeTwo.transformationMethod = AsteriskPasswordTransformationMethod()
        binding.codeThree.transformationMethod = AsteriskPasswordTransformationMethod()
        binding.codeFour.transformationMethod = AsteriskPasswordTransformationMethod()
    }

}
