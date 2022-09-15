package io.eclectics.cargilldigital.ui.auth.pinmgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentPinBinding
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.Exception
import kotlin.properties.Delegates

@AndroidEntryPoint
class PinFragment : Fragment() {
    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private var input_one_EditText2: ImageView? = null
    private var input_two_EditText2: ImageView? = null
    private var input_three_EditText2: ImageView? = null
    private var input_four_EditText2: ImageView? = null
   // private var input_five_EditText2: ImageView? = null

    lateinit var delete_pin_pad: ImageView
    private var one1: String? = null
    lateinit var onBackPressedCallback: OnBackPressedCallback
    /**
     * The Two.
     */
    private var two2: String? = null

    /**
     * The Three.
     */
    private var three3: String? = null

    /**
     * The Four.
     */
    private var four4: String? = null
    /**
     * The Four.
     */
   // private var five5: String? = null
    private var mConfirmPin: String? = null
    val viewModel: PinViewModel by viewModels()
    lateinit var requestJson: JSONObject
    //check if request needs printing
    var isPrintableData by Delegates.notNull<Boolean>()

    @Inject
    lateinit var pdialog: SweetAlertDialog
    val genViewModel: GeneralViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_pin, container, false)
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var transJson = requireArguments().getString("requestJson")
        requestJson = JSONObject(transJson)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        input_one_EditText2 = view.findViewById(R.id.input_one)
        input_two_EditText2 = view.findViewById(R.id.input_two)
        input_three_EditText2 = view.findViewById(R.id.input_three)
        input_four_EditText2 = view.findViewById(R.id.input_four)
        //input_five_EditText2 = view.findViewById(R.id.input_five)
        delete_pin_pad = view.findViewById(R.id.delete_pin_pad)
        initUI()
        setUpOnBackPress()

        binding.pinPadOk.setOnClickListener {

            if (mConfirmPin?.isNotBlank() == true) {
                requestJson.put("pin", mConfirmPin)
                var endpoint = requestJson.getString("endPoint")//ApiEndpointObj.verifyAddTelcoAccount
                // (activity as MainActivity?)!!.navigationMgmt()
                CoroutineScope(Dispatchers.Main).launch {
                    pdialog.show()
                    genViewModel.generalPinRequest(requestJson, endpoint,requireActivity())
                        .observe(requireActivity(), Observer {
                            pdialog.dismiss()
                            when (it) {
                                is ViewModelWrapper.error -> GlobalMethods().pinTransactionWarning(
                                    requireActivity(),
                                    "${it.error}"
                                )//LoggerHelper.loggerError("error","error")
                                is ViewModelWrapper.response -> processRequest(it.value)
                            //LoggerHelper.loggerSuccess("success","success ${it.value}")
                                //processRequest(it.value)//
                            }
                        })
                }

            }

        }
    }

    private fun setUpOnBackPress() {

         onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //NetworkUtility().transactionWarning("You wantto exit?",requireActivity())
                try {
                    var currentProfileDashboard =
                        UtilPreference().getActiveDashboard(requireActivity())
                    findNavController().navigate(currentProfileDashboard)
                    //(activity as MainActivity?)!!.navigationMgmt()
                }catch (ex:Exception){
                    findNavController().popBackStack()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,onBackPressedCallback)
        //binding.setLifecycleOwner(getViewLifecycleOwner());
    }


    private fun processRequest(response: String) {
        //check if data submited is printable
        isPrintableData = UtilPreference().isPrintableData(requireActivity())
    LoggerHelper.loggerError("reqresponse","response $response")
        try {
            var jsonResponse = JSONObject(response)
            var message = jsonResponse.getString("message")
          /*  if(isPrintableData){
                var intent = Intent(requireActivity(), PrinterActivity::class.java)
                intent.putExtra("printerdata","reddf")
                intent.putExtra("isSettings",false)
                requireActivity().startActivity(intent)
            }else{
                NetworkUtility().confirmPinRequestResponse(binding.root, message, requireActivity())
            }*/
            NetworkUtility().confirmPinRequestResponse(binding.root, message, requireActivity())



        }catch (ex:Exception){
            //TODO HII NI YA DEMO YA 23/03 REMOVE
            LoggerHelper.loggerError("errorpin","error ${ex.message}")
              //  var message = "Successful"
            //NetworkUtility().confirmPinRequestResponse(binding.root, response, requireActivity())
          /*  if(isPrintableData){//requestJson.getBoolean("printable")
                var intent = Intent(requireActivity(), PrinterActivity::class.java)
                intent.putExtra("printerdata","rttrer")
                intent.putExtra("isSettings",false)
                requireActivity().startActivity(intent)
            }else{
                NetworkUtility().confirmPinRequestResponse(binding.root, response, requireActivity())
            }*/
            NetworkUtility().confirmPinRequestResponse(binding.root, response, requireActivity())
        }

    }


    private fun initUI() {
       // binding.tvTitle.setText(getString(R.string.we_ve_sent_an_ottc_to, phone))
       // binding.miniTab.setText(label)
        binding.btnNumberOne.setOnClickListener { controlPinPad2("1") }
        binding.btnNumberTwo.setOnClickListener {  controlPinPad2("2") }
        binding.btnNumberThree.setOnClickListener {controlPinPad2("3") }
        binding.btnNumberFour.setOnClickListener {  controlPinPad2("4") }
        binding.btnNumberFive.setOnClickListener {  controlPinPad2("5") }
        binding.btnNumberSix.setOnClickListener {  controlPinPad2("6") }
        binding.btnNumberSeven.setOnClickListener {  controlPinPad2("7") }
        binding.btnNumberEight.setOnClickListener {  controlPinPad2("8") }
        binding.btnNumberNine.setOnClickListener {  controlPinPad2("9") }
        binding.btnNumberZero.setOnClickListener {  controlPinPad2("0") }
        binding.deletePinPad.setOnClickListener {  deletePinEntry() }
    }
    private fun controlPinPad2(entry: String) {
        if (one1 == null) {
            input_one_EditText2!!.setImageResource(R.mipmap.ic_star)
            one1 = entry
        } else if (two2 == null) {
            input_two_EditText2!!.setImageResource(R.mipmap.ic_star)
            two2 = entry
        } else if (three3 == null) {
            input_three_EditText2!!.setImageResource(R.mipmap.ic_star)
            three3 = entry
        } else if (four4 == null) {
            input_four_EditText2!!.setImageResource(R.mipmap.ic_star)
            four4 = entry
        }
      /*  else if (five5 == null) {
            input_five_EditText2!!.setImageResource(R.mipmap.ic_star)
            five5 = entry
        }
*/
        if (mConfirmPin == null) {
            mConfirmPin = entry
        } else {
            if(mConfirmPin!!.length <4) {
                mConfirmPin = mConfirmPin + entry
            }
        }
        if (mConfirmPin!!.length == 4) {
            binding.pinPadOk!!.isEnabled = true
        }
    }
    /**
     * pinpad control
     * @param entry the entry
     */

    private fun deletePinEntry() {
        if (mConfirmPin != null && mConfirmPin!!.length > 0) {
            mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
        }
        if (four4 != null) {
            input_four_EditText2!!.setImageResource(R.mipmap.ic_underscore)
            four4 = null
        } else if (three3 != null) {
            input_three_EditText2!!.setImageResource(R.mipmap.ic_underscore)
            three3 = null
        } else if (two2 != null) {
            input_two_EditText2!!.setImageResource(R.mipmap.ic_underscore)
            two2 = null
        } else if (one1 != null) {
            input_one_EditText2!!.setImageResource(R.mipmap.ic_underscore)
            one1 = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
          onBackPressedCallback.remove()
    }

}