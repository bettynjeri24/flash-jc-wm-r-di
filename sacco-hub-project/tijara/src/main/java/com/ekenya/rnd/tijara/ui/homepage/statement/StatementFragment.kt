package com.ekenya.rnd.tijara.ui.homepage.statement

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.requestDTO.FullStatementDTO
import com.ekenya.rnd.tijara.databinding.DepositDialogLayoutBinding
import com.ekenya.rnd.tijara.databinding.FragmentStatementBinding
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.utils.FieldValidators
import com.ekenya.rnd.tijara.utils.onInfoDialog
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.spinkit_dialog_loading.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StatementFragment : BaseDaggerFragment() {
    lateinit var binding: FragmentStatementBinding
    lateinit var cardBinding: DepositDialogLayoutBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val fullViewmodel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(FullStatementViewModel::class.java)
    }
    private var formattedStartDateApi: String =""
    private var formattedDateApi : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentStatementBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viewmodel=fullViewmodel
        setupNavUp()
        with(binding.content) {
            etEmail.setText(Constants.EMAILADDRESS)
            val cal: Calendar = Calendar.getInstance()
            val currentTime: Date = cal.time
            cal.add(Calendar.DAY_OF_YEAR, -30)
            val startDate = cal.time

            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
            val formattedstartDate = formatter.format(currentTime)
            val formattedendDate = formatter.format(startDate)

            formattedStartDateApi = startDate.toString().trim()
            formattedDateApi = currentTime.toString().trim()

            etstartdate.setText(formattedendDate)
            etenddate.setText(formattedstartDate)
        }

        fullViewmodel.statusCode.observe(viewLifecycleOwner, Observer {
            if (null!=it){
                binding.progressbar.visibility=View.GONE
                when(it){
                    1->{
                        binding.progressbar.visibility=View.GONE
                        Constants.isFROMPESALINKPHONE=7
                        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
                        cardBinding= DepositDialogLayoutBinding.inflate(LayoutInflater.from(context))
                        cardBinding.apply {
                            fullViewmodel.fullStatProperties.observe(viewLifecycleOwner, Observer {fullstat->
                                tvHeading.text=getString(R.string.confirm_get_statement)
                                tvName.text=getString(R.string.account)
                                val accNumber=Constants.SAVINGACCOUNTNO.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
                                tvNameValue.text="${Constants.SAVINGACCOUNTNAME} - A/C $accNumber"
                                tvBank.text=getString(R.string.start_date)
                                Constants.STARTDATE=fullstat.from
                                Constants.ENDDATE=fullstat.to
                                tvBankValue.text=fullstat.from
                                tvACNO.text=getString(R.string.end_date)
                                tvACNOValue.text=fullstat.to
                                tvAmount.text=getString(R.string.charges)
                                tvAmountValue.text=fullstat.charges.toString()
                                Constants.FULLSCHARGES=fullstat.charges
                                tvFrom.visibility=View.GONE
                                tvFromValur.visibility=View.GONE
                            })

                        }

                        cardBinding.btnCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        cardBinding.btnSubmit.setOnClickListener {
                            dialog.dismiss()
                            binding.progressbar.visibility=View.VISIBLE
                            binding.progressbar.tv_pbTitle.visibility=View.GONE
                            binding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                            val statementDTO=StatementDTO()
                            statementDTO.formId=Constants.FULLSFORMID
                            fullViewmodel.generateFullStatement(statementDTO)
                            fullViewmodel.stopObserving()
                        }

                        dialog.setContentView(cardBinding.root)
                        dialog.show()
                        dialog.setCancelable(false)

                    }
                    0->{
                        fullViewmodel.stopObserving()
                        binding.progressbar.visibility=View.GONE
                        onInfoDialog(requireContext(),fullViewmodel.statusMessage.value)

                    }
                    else->{
                        fullViewmodel.stopObserving()
                        binding.progressbar.visibility=View.GONE
                        onInfoDialog(requireContext(),getString(R.string.error_occurred))

                    }
                }
            }
        })
        fullViewmodel.statusCommit.observe(viewLifecycleOwner, Observer {
            if (null!=it){
                fullViewmodel.stopObserving()
                binding.progressbar.visibility=View.GONE
                when(it){
                    1->{
                        fullViewmodel.stopObserving()
                        binding.progressbar.visibility=View.GONE
                        Constants.isFROMPESALINKPHONE=7
                        fullViewmodel.stopObserving()
                        findNavController().navigate(R.id.action_statementFragment_to_statementSuccessFragment)
                    }
                    0->{
                        fullViewmodel.stopObserving()
                        binding.progressbar.visibility=View.GONE
                        onInfoDialog(requireContext(),fullViewmodel.statusCMessage.value)

                    }
                    else->{
                        fullViewmodel.stopObserving()
                        binding.progressbar.visibility=View.GONE
                        onInfoDialog(requireContext(),getString(R.string.error_occurred))

                    }
                }
            }
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**spinner impl*/
        with(binding.content) {
            //load calender
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            etstartdate.setOnClickListener {
                val datepicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selecteddate = Calendar.getInstance()
                    selecteddate.set(Calendar.YEAR,year)
                    selecteddate.set(Calendar.MONTH,month)
                    selecteddate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    val formattedDate = formatter.format(selecteddate.time)
                    val formatterApi = SimpleDateFormat("yyyyMMddHHmmss", Locale.UK)
                  formattedStartDateApi = formatterApi.format(selecteddate.time)
                    etstartdate.setText(formattedDate)
                },year, month, day)
                datepicker.show()
            }
            etenddate.setOnClickListener {
                val datepicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selecteddate = Calendar.getInstance()
                    selecteddate.set(Calendar.YEAR,year)
                    selecteddate.set(Calendar.MONTH,month)
                    selecteddate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                    val formatterApi = SimpleDateFormat("yyyyMMddHHmmss", Locale.UK)
                    val formattedDate = formatter.format(selecteddate.time)
                   formattedDateApi = formatterApi.format(selecteddate.time)
                    etenddate.setText(formattedDate)
                },year, month, day)
                //

                datepicker.show()
            }
            btnstatement.setOnClickListener {
                val startDate=etstartdate.text.toString()
                val endDate=etenddate.text.toString()
                val emailAdress=(binding.content.etEmail.text.toString())
                val validMail=FieldValidators().isEmailValid(emailAdress)
                val validMsg = FieldValidators.VALIDINPUT
                if (!validMail.contains(validMsg)){
                    binding.content.tlEmail.error=validMail
                }else if (startDate.isEmpty()){
                    tlEmail.error=""
                    tlStartDate.error="Select start date"
                }else if (endDate.isEmpty()){
                    tlStartDate.error=""
                    tlEndDate.error="Select end date"
                }else{
                    tlEndDate.error=""
                    val fullStatementDTO=FullStatementDTO()
                    fullStatementDTO.accountId= Constants.SAVINGAID
                    fullStatementDTO.from=startDate
                    fullStatementDTO.to=endDate
                    fullStatementDTO.recipientEmail=emailAdress
                    binding.progressbar.visibility=View.VISIBLE
                    binding.progressbar.tv_pbTitle.visibility=View.GONE
                    binding.progressbar.tv_pbTex.text=getString(R.string.please_wait)
                    fullViewmodel.getFullStatement(fullStatementDTO)
                }
            }

        }


    }

    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.statements)
    }

}