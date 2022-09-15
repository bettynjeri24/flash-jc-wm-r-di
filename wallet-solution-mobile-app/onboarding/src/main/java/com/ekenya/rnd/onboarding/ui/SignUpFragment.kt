package com.ekenya.rnd.onboarding.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Utils
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.SignUpFragmentBinding
import com.google.android.material.textfield.TextInputLayout


class SignUpFragment : BaseDaggerFragment() {
    private var hasAgreedtoTerms: Boolean = false
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var fullname: String
    private lateinit var model: TransactionConfirmationViewModel
    private lateinit var phoneNumber: String
    private lateinit var email: String
    private lateinit var names: List<String>
    private lateinit var signUpViewModel: SignUpViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initializeUi()
        initializeViewModel()
        initClickListeners()

        initializeObservers()

        return binding.root
    }

    private fun initClickListeners() {

        binding.tvTermsNConditions.setOnClickListener {
            findNavController().navigate(R.id.aboutUsFragment2)
        }


        binding.checkboxTerms.setOnCheckedChangeListener { _, isChecked ->
            hasAgreedtoTerms = isChecked
        }
    }

    private fun initializeObservers() {
        signUpViewModel.firstName.observe(viewLifecycleOwner, {
            //validate the string
            validateString(binding.tilFullName, it)
        })
    }

    private fun initializeViewModel() {
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        model =
            ViewModelProvider(requireActivity()).get(TransactionConfirmationViewModel::class.java)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeUi() {


        binding = SignUpFragmentBinding.inflate(layoutInflater)
        hasAgreedtoTerms = false
        binding.checkboxTerms.isChecked = false
        setHasOptionsMenu(true)

        /* binding.etPhonenumber.doAfterTextChanged {
             signUpViewModel.setFirstName(it.toString())
         }*/


        binding.btnContinue.setOnClickListener {
            if (validDetails()) {


                saveData(names, email)
              /*  Navigation.findNavController(it)
                    .navigate(R.id.action_signUpFragment_to_verifyIdentityFragment)*/

                findNavController().navigate(R.id.manualVerificationFragment)
                hasAgreedtoTerms = false
                binding.checkboxTerms.isChecked = false
            } else {
                /* Toast.makeText(
                     context,
                     "Check on your Internet Connectivity",
                     Toast.LENGTH_LONG
                 ).show()*/

            }


        }

    }

    private fun saveData(names: List<String>, email: String) {
        if (names.size == 3) {

            context?.let { it1 -> SharedPreferencesManager.setFirstName(it1, names[0]) }
            context?.let { it1 -> SharedPreferencesManager.setMiddleName(it1, names[1]) }
            context?.let { it1 -> SharedPreferencesManager.setLastName(it1, names[2]) }
        } else if (names.size == 2) {
            context?.let { it1 -> SharedPreferencesManager.setFirstName(it1, names[0]) }
            context?.let { it1 -> SharedPreferencesManager.setLastName(it1, names[1]) }
            context?.let { it1 -> SharedPreferencesManager.setMiddleName(it1, "") }

        }
        context?.let { it1 -> SharedPreferencesManager.setEmailAddress(it1, email) }
    }

    private fun validateString(v: TextInputLayout, data: String): Boolean {
        if (data.isNullOrBlank()) {
            v.error = null
            v.isErrorEnabled = false
            v.error = "Please enter a valid Name"
            return false
        }/*else{
            names = fullname.split("\\s".toRegex())
            if (names.size < 2){
                binding.tilFullName.error = null
                binding.tilFullName.isErrorEnabled = false
                binding.tilFullName.error = "Please enter at least two names"
                return false

            }
        }*/

        return true
    }

    private fun validDetails(): Boolean {
        fullname = binding.etFullname.text.toString().trim()
        phoneNumber = SharedPreferencesManager.getPhoneNumber(requireContext()).toString()
        email = binding.etEmail.text.toString().trim()
        if (fullname.isNullOrBlank()) {
            binding.tilFullName.error = null
            binding.tilFullName.isErrorEnabled = false
            binding.tilFullName.error = "Please enter a valid Name"
            return false
        } else {
            names = fullname.split("\\s".toRegex())
            if (names.size < 2) {
                binding.tilFullName.error = null
                binding.tilFullName.isErrorEnabled = false
                binding.tilFullName.error = "Please enter at least two names"
                return false

            }
        }

        if (email.isNullOrBlank() || !Utils.isEmailValid(email)) {
            binding.tilEmailAddress.error = "Please enter a valid email address"
            binding.tilEmailAddress.error
            return false
        }
        if (!hasAgreedtoTerms) {
            showErrorSnackBar("Please Check the terms and conditions")
            return false
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_help -> {
                toastMessage("help clicked")
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }


    override fun onResume() {
        super.onResume()
        removeActionbarTitle()
        changeActionbarColor(R.color.white)
        removeActionBarElevation()
        showSupportActionBar()
        hasAgreedtoTerms = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val upArrow = getResources().getDrawable(R.drawable.ic_back_arrow_new);
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
    }

    override fun onStop() {
        super.onStop()

    }
}