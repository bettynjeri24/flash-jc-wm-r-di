package com.ekenya.rnd.baseapp.ui.deeplinking

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ekenya.rnd.baseapp.databinding.ActivityFarmForceSenderMainBinding
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.utils.custom.generateMyPassKey
import com.ekenya.rnd.common.utils.custom.showCargillCustomSuccessDialog
import com.ekenya.rnd.common.utils.custom.showCargillCustomWarningDialog
import com.ekenya.rnd.common.utils.custom.uuidStringToBigIntPositive
import timber.log.Timber

class FarmForceSenderMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFarmForceSenderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmForceSenderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rands = (1111111111111111..9999999999999999).random()
        Timber.e("RANDOM PURCHASE ID IS: $rands")
        binding.etPurchaseId.setText(rands.toString())
        binding.etPaymentType.setText("COCOA PAYMENT")
        binding.etAmount.setText("1500")
        binding.etFarmerPhoneNumber.setText("2250701686379")
        binding.etBuyerPhonenumber.setText("2250703035850")
        binding.etComments.setText("pay")

        binding.btnHome.setOnClickListener {
            if (isvalidField()) {
                Timber.e("GOOD")
                parseIntentDataToCargillApp()
            } else {
                Timber.e("BAD")
            }
        }
        // 6215871e-543b-4f2d-889a-639f47dce3c0
        Timber.e("UUID ${uuidStringToBigIntPositive("6215871e-543b-4f2d-889a-639f47dce3c0")}")
    }

    private fun parseIntentDataToCargillApp() {
        try {
            val uri =
                Uri.parse(
                    "cargill://cargill-api.eclectics.io/cargill?" +
                        "purchaseId=${binding.etPurchaseId.text}&" +
                        "paymentType=1&" +
                        "amount=${binding.etAmount.text}&" +
                        "buyerPhonenumber=${binding.etBuyerPhonenumber.text}&" +
                        "farmerPhonenumber=${binding.etFarmerPhoneNumber.text}&" +
                        "paymentKey=${
                        generateMyPassKey(
                            (System.currentTimeMillis() / 1000L),
                            binding.etAmount.text.toString()
                        )
                        }&" +
                        "DateTime=0001-01-01T00:00:00&" +
                        "comments=${binding.etComments.text}"
                )
            Timber.e("URL $uri \nQUERY ${uri.query}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            // startActivity(intent)
            resultLauncher.launch(intent)
        } catch (e: Exception) {
            Timber.e("137 FAILED TO OPEN CARGILL APP ${e.message}")
            dialogToPromptDownloadApp()
        }
    }

    private fun startCargillAppIntent(intent: Intent) {
        // Verify if receiver app XXX this screen path
        val packageManager: PackageManager = this.applicationContext.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe = activities.size > 0
        if (isIntentSafe) {
            // startActivityForResult(intent, 2)
            resultLauncher.launch(intent)
        } else {
            Timber.e("132 FAILED TO OPEN CARGILL APP ")
            // dialogToPromptDownloadApp()
        }
    }

    private fun dialogToPromptDownloadApp() {
        toast("FAILED TO OPEN CARGILL APP")
        this.showCargillCustomWarningDialog(
            title = getString(com.ekenya.rnd.common.R.string.error_message),
            description = "Please Install Cargill Digital App to Send Data",
            btnConfirmText = "Okay",
            action = {
                try {
                    Timber.e("149 FAILED TO OPEN CARGILL APP ")
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    Timber.e("157 FAILED TO OPEN CARGILL APP ${e.message}")
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")

                        )
                    )
                }
            }
        )
    }

    private fun isvalidField(): Boolean {
        if (binding.etPurchaseId.text.toString().isEmpty()) {
            binding.etPurchaseId.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (binding.etPaymentType.text.toString().isEmpty()) {
            binding.etPaymentType.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_payment)
            return false
        }
        if (binding.etAmount.text.toString().isEmpty()) {
            binding.etAmount.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_amount)
            return false
        }
        if (binding.etFarmerPhoneNumber.text.toString().isEmpty()) {
            binding.etFarmerPhoneNumber.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_phoneNo)
            return false
        } else {
            return true
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val message = data?.getStringExtra("Message")
                Timber.e("FF SIMULATOER => $data")
                Timber.e("FF SIMULATOER => ${data?.getStringExtra("APIRESPONSE")}")
                toast("MESSAGE FROM INTENT IS :\n$message")
                Timber.e(message)
                if (message == "SUCCESS_TRANSACTION_RECIEVED") {
                    this.showCargillCustomSuccessDialog(
                        description = message.toString(),
                        positiveButtonFunction = {
                            binding.etPurchaseId.setText("")
                            binding.etPaymentType.setText("")
                            binding.etAmount.setText("")
                            binding.etFarmerPhoneNumber.setText("")
                            binding.etComments.setText("")
                        }
                    )
                } else {
                    this.showCargillCustomWarningDialog(
                        description = message.toString()
                    )
                }
            } else {
                this.showCargillCustomWarningDialog()
            }
        }
}
