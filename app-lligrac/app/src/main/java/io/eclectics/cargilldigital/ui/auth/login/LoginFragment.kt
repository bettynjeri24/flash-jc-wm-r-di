package io.eclectics.cargilldigital.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.LoginFragmentBinding
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import org.json.JSONObject
import java.math.BigInteger
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var navoption: NavOptions

    @Inject
    lateinit var viewModel: AuthViewModel //by viewModels()

    // @Inject
    lateinit var pDialog: SweetAlertDialog
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var account: String
    private val globalMethods = GlobalMethods()

    private lateinit var user: String
    private lateinit var pass: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        navoption = GlobalMethods.navigation.options()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //coded creds
        binding.etUsername.setText("test@test.com")
        binding.etPassword.setText("12345")

        /* user = UserRepository.theUser.value!!.userID
         pass= UserRepository.theUser.value!!.password*/

        pDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)

        val args = requireArguments()
        account = args.getString("accountType", "agent")

        if (account == "agent") {
            binding.ivUserType.setImageDrawable(resources.getDrawable(R.mipmap.agent_icon))
            binding.tvUsername.text = getString(R.string.buyer_id)
        } else if (account == "generalwallet") {
            binding.ivUserType.setImageDrawable(resources.getDrawable(R.mipmap.agrovets))
            binding.tvUsername.text = getString(R.string.other_user)
        } else if (account == "cooperative") {
            binding.ivUserType.setImageDrawable(resources.getDrawable(R.drawable.agent))
            binding.tvUsername.text = getString(R.string.cooperative_id)
        } else {
            binding.ivUserType.setImageDrawable(resources.getDrawable(R.drawable.farmer))
            binding.tvUsername.text = getString(R.string.farmer_id)
        }

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it)
                navigate()
        })

        binding.btnLogin.setOnClickListener {
            if (valid()) {
                //test big integer
                var mainStrinfgtest = "290f98"
                val inputStringBytes: ByteArray = mainStrinfgtest.toByteArray()
                val result = BigInteger(inputStringBytes)
                LoggerHelper.loggerError("bigints", "$result and ${result.toByteArray()}")
                // assertEquals("290f98", String(result.toByteArray()))
                viewModel.navigate()

                /* //send server request
                     var transactionJson =JSONObject()
                  lifecycleScope.launch {
                     transactionJson.put("email", binding.etUsername.text.toString().trim())
                     transactionJson.put("password", binding.etPassword.text.toString().trim())
                     NetworkUtility().sendRequest(pDialog)
                     sendLoginRequest(transactionJson)
                 }*/


            }
        }
    }

    private suspend fun sendLoginRequest(dataJson: JSONObject) {
        viewModel.sendloginRequest(dataJson).observe(requireActivity(), Observer {
            pDialog.dismiss()
            when (it) {
                is ViewModelWrapper.error -> GlobalMethods().transactionWarning(
                    requireActivity(),
                    "${it.error}"
                )//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })
    }

    private fun processRequest(value: String) {
        LoggerHelper.loggerError("resposne", "response $value")
        //save to sharedPref

        UtilPreference().saveLoggedAgent(requireContext(), value)
        viewModel.navigate()
    }

    private fun valid(): Boolean {
        userName = binding.etUsername.text.toString().toUpperCase()
        password = binding.etPassword.text.toString()

        if (userName.isEmpty()) {
            binding.etUsername.error = getString(R.string.input_required)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.input_required)
            return false
        }

        /* if (userName != user || pass != password){
             globalMethods.transactionWarning(requireActivity(), getString(R.string.invalid_credentials))
             return false
         }*/

        // globalMethods.loader(requireActivity())

        return true
    }

    private fun navigate() {
        if (account == "agent") {
            //  findNavController().navigate(R.id.nav_buyerDashboard,null, navoption)
            findNavController().navigate(R.id.nav_agentProfile, null, navoption)
        }//nav_farmerDashboard
        else if (account == "cooperative") {
            findNavController().navigate(R.id.nav_cooperativeProfile, null, navoption)
        } else if (account == "generalwallet") {
            findNavController().navigate(R.id.nav_generalWalletProfile, null, navoption)
        } else {
            // findNavController().navigate(R.id.farmerHomeFragment,null, null)
            var bundle = Bundle()
            bundle.putString("menu", "mainmenu")
            findNavController().navigate(R.id.nav_farmerDashboard, bundle, navoption)
            //agrovet_navigation nav_farmerDashboard
        }
        viewModel.hasNavigated()
/*

            else if(account == "agrovet"){
            findNavController().navigate(R.id.nav_agrovetMain,null, null)
        }
            else if(account == "supervisor"){
                findNavController().navigate(R.id.agentFragment2,null, null)
            }


*/
    }


}