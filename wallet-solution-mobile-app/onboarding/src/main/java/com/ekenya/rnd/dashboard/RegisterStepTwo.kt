package com.ekenya.rnd.dashboard

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.RegisterUserReq
import com.ekenya.rnd.dashboard.datadashboard.model.RegisterUserResp
import com.ekenya.rnd.dashboard.datadashboard.remote.APIErrorResponse
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.AppUtils.afterTextChanged
import com.ekenya.rnd.dashboard.utils.Constants
import com.ekenya.rnd.dashboard.utils.hideSupportActionBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarTransparent
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentRegisterStepTwoBinding

class RegisterStepTwo : Fragment() {

    lateinit var binding: FragmentRegisterStepTwoBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dialog: Dialog
    private var formattedPhoneNo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)

        initUI()
        setupViewModel()
        addTextChangeListeners()
        initClickListener()
        disablePasteOnEditText()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            formattedPhoneNo = requireArguments().getString("formattedPhoneNo").toString()
            SharedPreferencesManager.setPhoneNumber(requireContext(), formattedPhoneNo)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeStatusBarTransparent()
        hideSupportActionBar()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ),
                ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
    }

    private fun sendUserSignupReq(userReq: RegisterUserReq) {
        Constants.callDialog2("Registering...", requireContext())
        loginViewModel.registerUser(userReq).observe(viewLifecycleOwner) { myAPIResponse ->
            if (myAPIResponse.requestName == "RegisterUserReq") {
                Constants.cancelDialog()

                if (myAPIResponse.code == 200) {
                    val resp = myAPIResponse.responseObj as RegisterUserResp
                    successRegister("Info", resp.message)
                } else {
                    if (myAPIResponse.responseObj == null) {
                        Toast.makeText(requireContext(), myAPIResponse.getMessage(), Toast.LENGTH_LONG).show()
                    } else {
                        val error = myAPIResponse.responseObj as APIErrorResponse
                        Toast.makeText(requireContext(), error.error_description, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun successRegister(
        title: String,
        message: String
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(
                "OK"
            ) { dialog, _ -> //
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    private fun addTextChangeListeners() {
        binding.tiIdnumber.editText!!.afterTextChanged {
            binding.tiIdnumber.error = null
        }

        binding.tlSurname.editText!!.afterTextChanged {
            binding.tlSurname.error = null
        }

        binding.tiMiddlename.editText!!.afterTextChanged {
            binding.tiMiddlename.error = null
        }

        binding.tiLastname.editText!!.afterTextChanged {
            binding.tiLastname.error = null
        }

        binding.tiEmail.editText!!.afterTextChanged {
            binding.tiEmail.error = null
        }
    }

    private fun initClickListener() {
        binding.btnContinue.setOnClickListener {
            val tlSurname = binding.tlSurname.editText!!.text.toString()
            val tiMiddlename = binding.tiMiddlename.editText!!.text.toString()
            val tiLastname = binding.tiLastname.editText!!.text.toString()
            val email = binding.tiEmail.editText!!.text.toString()
            val gender = "M"
            val password = "0000"

            sendUserSignupReq(
                RegisterUserReq(
                    tlSurname,
                    tiMiddlename,
                    tiLastname,
                    formattedPhoneNo,
                    email,
                    gender,
                    password,
                    "123456789"
                )
            )
        }
    }

    private fun initUI() {
        dialog = Dialog(requireContext())

        binding.toolbar.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )

        binding.tvTerms.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://chama24.co.ke/#/home")
            startActivity(i)
        }

        //   (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_arrow)
//        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

    private fun disablePasteOnEditText() {
        binding.tiIdnumber.editText!!.customSelectionActionModeCallback =
            object : ActionMode.Callback {
                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }

        binding.tlSurname.editText!!.customSelectionActionModeCallback =
            object : ActionMode.Callback {
                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }

        binding.tiMiddlename.editText!!.customSelectionActionModeCallback =
            object : ActionMode.Callback {
                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }

        binding.tiLastname.editText!!.customSelectionActionModeCallback =
            object : ActionMode.Callback {
                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }

        binding.tiEmail.editText!!.customSelectionActionModeCallback =
            object : ActionMode.Callback {
                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return false
                }
            }
    }
}
