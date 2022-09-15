package com.ekenya.rnd.authcargill.ui.pinmgmt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentChangePinBinding
import com.ekenya.rnd.common.utils.custom.CustomTextWatcher
import com.ekenya.rnd.common.utils.custom.isValidPIN


class ChangePinFragment : Fragment() {
    private var _binding: FragmentChangePinBinding? = null
    private val binding get() = _binding!!

    lateinit var pdialog: SweetAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //1  return inflater.inflate(R.layout.fragment_change_pin, container, false)
        _binding = FragmentChangePinBinding.inflate(inflater, container, false)
       // (activity as MainActivity?)!!.hideToolbar()
       // ToolBarMgmt.setToolbarTitle(resources.getString(R.string.reset_pin),resources.getString(R.string.verify_set_pin),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        setListeners()
        binding.btnContinue.setOnClickListener {
            //findNavController().navigate(R.id.nav_selectAccount)
            if(isValidFields()) {
                sendSetPinReq()
            }
        }

    }

    private fun setListeners() {
        binding.tlOldPassword.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlOldPassword))
        binding.tlPassword.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlPassword))
        binding.tlConfirmPassword.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlConfirmPassword))
    }

    private fun isValidFields(): Boolean {

        var pin = binding.etPassword.text.toString()
        var confirmPin = binding.etConfirmPassword.text.toString()
        var oldPin = binding.etOldPassword.text.toString()

        if( isValidPIN(pin)) {
            binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
        return false
        }
        if( isValidPIN(confirmPin)) {
            binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        if( isValidPIN(confirmPin)) {
            binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        if(!pin.trim().contentEquals(confirmPin.trim())){
           binding.etConfirmPassword.requestFocus()
            binding.tlConfirmPassword.error = resources.getString(com.ekenya.rnd.common.R.string.new_confirm_notmatch)
            return false
        }
        if( isValidPIN(oldPin)) {
            binding.etOldPassword.requestFocus()
            binding.tlOldPassword.error = resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        if(oldPin == pin){
            binding.etPassword.requestFocus()
            binding.tlPassword.error = resources.getString(com.ekenya.rnd.common.R.string.new_old_cannotbe_same)
            return false
        }

        return true

    }

    private fun sendSetPinReq() {

    }

}