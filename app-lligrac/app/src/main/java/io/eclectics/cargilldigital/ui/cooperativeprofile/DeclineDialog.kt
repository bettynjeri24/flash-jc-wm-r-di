package io.eclectics.cargilldigital.ui.cooperativeprofile

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.google.android.material.button.MaterialButton
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.DialogDeclineFtopupBinding

object DeclineDialog {

    fun showDialog(context:Activity, message: String, title: String, operation: Unit) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE) // before

        val layoutInflater = LayoutInflater.from(context)
        var binding = DialogDeclineFtopupBinding.inflate(layoutInflater)
        // dialog?.setContentView(R.layout.dialog_decline_ftopup)

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        // lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.setContentView(binding.root)

        binding.tvMessage.text = message
        // dialog.findViewById<TextView>(R.id.tvMessage). = message
        //binding.btnOk.text = "Proceed"
        var btnok: MaterialButton = dialog?.findViewById(R.id.btn_ok)!!
        btnok.setOnClickListener {
            dialog.dismiss()
           // refectFundsRequest()
            operation.run {  }           // findNavController().navigate(R.id.nav_coolerOnboarding,null,navOptions)
        }
        // binding.btnCancel.setBackgroundColor(resources.getColor(R.color.reddish))
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
            //TODO ONCE ALL OUTLETS ARE ONBOARDED , USE THIS POPSTACK SINCE THE FRAGMENT WILL BE IN STACK
            //findNavController().popBackStack(R.id.nav_salesMenu,false)
            //findNavController().navigate(R.id.nav_salesMenu,null,navOptions)
        }

        dialog.show()

    }
}


