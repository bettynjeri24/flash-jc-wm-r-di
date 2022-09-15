package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PesalinkPhoneCheckFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.PesalinkPhoneCheckDTO
import com.ekenya.rnd.tijara.utils.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import timber.log.Timber

class PesalinkPhoneCheckFragment : Fragment() {
    private lateinit var phonecheckBinding:PesalinkPhoneCheckFragmentBinding
    private lateinit var viewModel: PesalinkPhoneCheckViewModel
    private var listPhone=""
    private var isCardSelected = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PesalinkPhoneCheckViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        phonecheckBinding= PesalinkPhoneCheckFragmentBinding.inflate(layoutInflater)
        phonecheckBinding.apply {
            /*etPhone.post(Runnable {
                val layoutParams: ViewGroup.LayoutParams = codePicker.getLayoutParams()
                layoutParams.height = etPhone.height
                codePicker.layoutParams = layoutParams
            })*/
        }

        return phonecheckBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("contact")?.observe(viewLifecycleOwner) {result ->
            phonecheckBinding.content.etPhone.setText(result)
            listPhone=result

        }
        with(phonecheckBinding.content) {
            btnPhoneCheck.setOnClickListener {
                val validMsg = FieldValidators.VALIDINPUT
                val phoneNumber = FieldValidators().formatPhoneNumber(etPhone.text.toString())
                val validPhone = FieldValidators().validPhoneNUmber(phoneNumber)
                if (!validPhone.contentEquals(validMsg)){
                    etPhone.requestFocus()
                    tlPhone.isErrorEnabled=true
                    tlPhone.error=validPhone
                }else{
                    tlPhone.error=""
                    val pesalinkPhoneCheckDTO =PesalinkPhoneCheckDTO()
                    pesalinkPhoneCheckDTO.phoneNumber=phoneNumber
                    phonecheckBinding.progressr.visibility=View.VISIBLE
                    phonecheckBinding.progressr.tv_pbTitle.visibility=View.GONE
                    phonecheckBinding.progressr.tv_pbTex.text=getString(R.string.please_wait)
                    viewModel.pesaLinkPhoneCheck(pesalinkPhoneCheckDTO)
                }

            }
        if (isCardSelected==1){
            isCardSelected=1
            Timber.d("CARD  $isCardSelected")
            rbMyself.isChecked=true
            rbOthers.isChecked=false
           etPhone.setText(Constants.PHONENUMBER)
           etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        }else {
            isCardSelected = 2
            rbMyself.isChecked = false
           rbOthers.isChecked
            Timber.d("CARD SELECTEEEEEEEEED $isCardSelected")
            etPhone.setCompoundDrawablesWithIntrinsicBounds(
                null, null,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_contacts_book), null
            )

           etPhone.onRightDrawableClicked {
                findNavController().navigate(R.id.listContactFragment)
            }
        }
            rbMyself.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    etPhone.setText(Constants.PHONENUMBER)
                    rbOthers.isChecked=false
                    isCardSelected=1
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
                }

            }
            rbOthers.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    rbMyself.isChecked=false
                    isCardSelected=2
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null,null,
                        ContextCompat.getDrawable(requireContext(),R.drawable.ic_contacts_book),null)
                   etPhone.setText(listPhone)
                   etPhone.onRightDrawableClicked {
                        findNavController().navigate(R.id.listContactFragment)
                    }

                }

            }
        }
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                phonecheckBinding.progressr.makeGone()
                viewModel.stopObserving()

                when (it) {

                    1 -> {
                    findNavController().navigate(R.id.action_pesalinkPhoneCheckFragment_to_sendToPhoneFragment)
                        viewModel.stopObserving()

                    }
                    0 -> {
                        onInfoDialog(requireContext(),viewModel.statusMessage.value)
                        phonecheckBinding.progressr.makeGone()
                        viewModel.stopObserving()
                    }
                    else -> {
                        onInfoDialog(requireContext(), getString(R.string.error_occurred))
                        phonecheckBinding.progressr.makeGone()
                        viewModel.stopObserving()

                    }
                }
            }
        })
        setupNavUp()
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        phonecheckBinding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        phonecheckBinding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.pesalink_to_phone)
    }




}