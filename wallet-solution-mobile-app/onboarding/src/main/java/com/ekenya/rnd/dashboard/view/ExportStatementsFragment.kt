package com.ekenya.rnd.dashboard.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentExportStatementsBinding

class ExportStatementsFragment : Fragment() {
    private lateinit var binding :FragmentExportStatementsBinding
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel:ConfirmSendingMoneyViewModel
    private lateinit var authorizeTransactionViewModel:AuthorizeTransactionViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExportStatementsBinding.inflate(inflater,container,false)
        initUi()
        initViewModels()
        setOnclickListeners()

        return binding.root
    }

    private fun initViewModels() {
        mobileWalletViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(MobileWalletViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)
    }

    private fun initUi() {
    //numPicker()

    }

/*
    private fun numPicker() {
        val mDatePicker =  DatePickerDialog(requireActivity(), android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedyear, int selectedmonth, int selectedday) {
                // Stuff
            }
        }, mYear, mMonth, mDayOfMonth) {

            final int month = getContext().getResources().getIdentifier("android:id/month", null, null);
            final String[] monthNumbers = new String[]{ "01","02","03","04","05","06","07","08","09","10","11","12"}

            @Override
            public void onDateChanged(@NonNull DatePicker view, int y, int m, int d) {
                super.onDateChanged(view, y, m, d);
                // Since DatePickerCalendarDelegate updates the month spinner too, we need to change months as numbers here also
                if(month != 0){
                    NumberPicker monthPicker = findViewById(month);
                    if(monthPicker != null){
                        monthPicker.setDisplayedValues(monthNumbers);
                    }
                }
            }

            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                // Hide day spinner
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if(day != 0){
                    NumberPicker dayPicker = findViewById(day);
                    if(dayPicker != null){
                        dayPicker.setVisibility(View.GONE);
                    }
                }
                // Show months as Numbers
                if(month != 0){
                    NumberPicker monthPicker = findViewById(month);
                    if(monthPicker != null){
                        monthPicker.setDisplayedValues(monthNumbers);
                    }
                }
            }
        };
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }
*/

    private fun setOnclickListeners() {
        binding.btnDownloadPdf.setOnClickListener{
            toastMessage("Feature Coming Soon")
        }
        binding.btnGenerateStatement.setOnClickListener{
            if(validDetails()){


                confirmSendingMoneyViewModel.setRequestingFragment(Constants.EXPORT_STATEMENTS)
                authorizeTransactionViewModel.setRequestingFragment(Constants.EXPORT_STATEMENTS)
                authorizeTransaction()
                //showEmailConfirmationDialog()


            }
        }
    }

    private fun showEmailConfirmationDialog() {

        val confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)


        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.succesful_transaction_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        val btnText = dialog.findViewById<TextView>(R.id.tv_phoneConfirmation)
        btnText.text="A notification will be sent via Email Once it is processed"



        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.navigation_home, false)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.navigation_home, false)

        }
        setDialogLayoutParams(dialog)
        dialog.show()


    }


    private fun authorizeTransaction() {
        findNavController().navigate(R.id.authorizeTransactionFragment)
    }


    private fun validDetails(): Boolean {
        return true

    }


}