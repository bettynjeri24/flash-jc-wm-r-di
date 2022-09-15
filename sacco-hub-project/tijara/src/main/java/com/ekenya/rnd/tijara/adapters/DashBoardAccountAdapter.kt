package com.ekenya.rnd.tijara.adapters

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.VerifyUserDialogBinding
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.requestDTO.VerifyUserDTO
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.VerifyUserViewModel
import com.ekenya.rnd.tijara.utils.onInfoDialog
import timber.log.Timber


class DashBoardAccountAdapter(val context: Context,
                              private val accountList: List<Accounts>): PagerAdapter() {
    var firstAccountNumber=-1
    var firstAccountId=-1
    private lateinit var dialogBinding: VerifyUserDialogBinding
    private lateinit var vUserviewModel: VerifyUserViewModel
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //pick single item within the list
        val myAccunt: Accounts =accountList[position]
        var v = View((context as AppCompatActivity).applicationContext)
        if (myAccunt.isShare==0) {
            v = LayoutInflater.from(container.context).inflate(
                R.layout.account_layout_row,
                container,
                false
            )
            firstAccountNumber = myAccunt.accountId
            firstAccountId= myAccunt.productId


            if (position == 0) {
                Constants.ACCOUNTID = firstAccountNumber
                Constants.SAVINGPRODUCTID = firstAccountId.toString().trim()
                Constants.SAVINGPRODUCTNAME = myAccunt.accountName.trim()
                Timber.d("FIRST NAME ${Constants.SAVINGPRODUCTNAME} ")
                Timber.d("FIRST ${Constants.ACCOUNTID}")
                Timber.d("FIRSTPRODUCT ${Constants.SAVINGPRODUCTID}")
            }
            val eyeToggle= v.findViewById<ImageView>(R.id.passToggle)
           /* eyeToggle.setOnClickListener {
                val dialog = Dialog(context)
                val wmlp = dialog.window!!.attributes
                wmlp.gravity = Gravity.TOP or Gravity.LEFT
                wmlp.x = 100 //x position
                wmlp.y = 150
                dialogBinding= VerifyUserDialogBinding.inflate(LayoutInflater.from(context))
                vUserviewModel= ViewModelProvider(context).get(VerifyUserViewModel::class.java)
                dialogBinding.vUserViewmodel=vUserviewModel
                dialogBinding.lifecycleOwner=context
                vUserviewModel.statusVCode.observe((context as AppCompatActivity), Observer {
                    if (null != it) {
                        when (it) {

                            1 -> {
                                (v.findViewById<View>(R.id.tv_Camount) as TextView).text =
                                    myAccunt.currentBalance
                                (v.findViewById<View>(R.id.tv_available_amount) as TextView).text =
                                    myAccunt.availableBalance
                                vUserviewModel.stopObserving()
                                dialog.hide()

                            }
                            0 -> {
                                onInfoDialog(context,vUserviewModel.statusMessage.value)
                                (v.findViewById<View>(R.id.tv_Camount) as TextView).text ="✽✽✽✽✽"
                                (v.findViewById<View>(R.id.tv_available_amount) as TextView).text ="✽✽✽✽✽"
                                dialog.hide()

                            }
                            else -> {
                                onInfoDialog(context, context.getString(R.string.error_occurred))
                                (v.findViewById<View>(R.id.tv_Camount) as TextView).text ="✽✽✽✽✽"
                                (v.findViewById<View>(R.id.tv_available_amount) as TextView).text ="✽✽✽✽✽"
                                dialog.hide()

                            }
                        }
                    }
                })
                dialogBinding.apply {
                    btnOkay.setOnClickListener {
                        val password = etPassword.text.toString()
                        if (password.isEmpty()) {
                            tlPass.error = context.getString(R.string.enter_your_password)
                        } else{
                            val verifyUserDTO = VerifyUserDTO()
                            verifyUserDTO.password = password
                            vUserViewmodel?.verifyUser(verifyUserDTO)

                        }
                    }
                }
                dialog.setContentView(dialogBinding.root)
                dialog.show()

            }*/

            (v.findViewById<View>(R.id.tv_product_name) as TextView).text = myAccunt.accountName
            (v.findViewById<View>(R.id.tv_kes_currency) as TextView).text =
                myAccunt.defaultCurrency
            (v.findViewById<View>(R.id.tv_Camount) as TextView).text =
                myAccunt.currentBalance
            (v.findViewById<View>(R.id.tv_available_amount) as TextView).text =
                myAccunt.availableBalance
            /*(v.findViewById<View>(R.id.tv_Camount) as TextView).text ="✽✽✽✽✽"
            (v.findViewById<View>(R.id.tv_available_amount) as TextView).text ="✽✽✽✽✽"*/
            (v.findViewById<View>(R.id.tv_def_currency) as TextView).text =
                myAccunt.defaultCurrency

            (v.findViewById<View>(R.id.tv_date) as TextView).text = myAccunt.lastSavingDate
        }else if (myAccunt.isShare==1){
            v = LayoutInflater.from(container.context).inflate(
                R.layout.share_layout_row,
                container,
                false
            )

            val eyeTogle= v.findViewById<ImageView>(R.id.eyeToggle)
           /* eyeTogle.setOnClickListener {
                val dialog = Dialog(context)
                val wmlp = dialog.window!!.attributes
                wmlp.gravity = Gravity.TOP or Gravity.LEFT
                wmlp.x = 100 //x position
                wmlp.y = 150
                dialogBinding= VerifyUserDialogBinding.inflate(LayoutInflater.from(context))
                vUserviewModel= ViewModelProvider(context).get(VerifyUserViewModel::class.java)
                dialogBinding.vUserViewmodel=vUserviewModel
                dialogBinding.lifecycleOwner=context

                vUserviewModel.statusVCode.observe(context, Observer {
                    if (null != it) {
                        when (it) {

                            1 -> {
                                (v.findViewById<View>(R.id.tv_dividend_amount) as TextView).text = myAccunt.dividend.toString()
                                (v.findViewById<View>(R.id.tv_dividend_rate) as TextView).text = myAccunt.dividendRate.toString()
                                vUserviewModel.stopObserving()
                                dialog.hide()

                            }
                            0 -> {
                                onInfoDialog(context,vUserviewModel.statusMessage.value)
                                (v.findViewById<View>(R.id.tv_dividend_amount) as TextView).text = "✽✽✽✽✽"
                                (v.findViewById<View>(R.id.tv_dividend_rate) as TextView).text = "✽✽✽✽✽"
                                dialog.hide()

                            }
                            else -> {
                                onInfoDialog(context, context.getString(R.string.error_occurred))
                                (v.findViewById<View>(R.id.tv_dividend_amount) as TextView).text = "✽✽✽✽✽"
                                (v.findViewById<View>(R.id.tv_dividend_rate) as TextView).text = "✽✽✽✽✽"
                                dialog.hide()

                            }
                        }
                    }
                })
                dialogBinding.apply {
                    btnOkay.setOnClickListener {
                        val password = etPassword.text.toString()
                        if (password.isEmpty()) {
                            tlPass.error = context.getString(R.string.enter_your_password)
                        } else{
                            val verifyUserDTO = VerifyUserDTO()
                            verifyUserDTO.password = password
                            vUserViewmodel?.verifyUser(verifyUserDTO)

                        }
                    }
                }
                dialog.setContentView(dialogBinding.root)
                dialog.show()

            }*/
            (v.findViewById<View>(R.id.tv_account_name) as TextView).text = myAccunt.accountName
            (v.findViewById<View>(R.id.tv_currency) as TextView).text = myAccunt.defaultCurrency
            (v.findViewById<View>(R.id.tv_dividend_amount) as TextView).text = myAccunt.dividend.toString()
            (v.findViewById<View>(R.id.tv_dividend_rate) as TextView).text = myAccunt.dividendRate.toString()
            (v.findViewById<View>(R.id.tv_Sharecurrency) as TextView).text = myAccunt.defaultCurrency
            (v.findViewById<View>(R.id.tv_shareCap) as TextView).text = myAccunt.shareCapital.toString()
            /*(v.findViewById<View>(R.id.tv_dividend_amount) as TextView).text = "✽✽✽✽✽"
            (v.findViewById<View>(R.id.tv_dividend_rate) as TextView).text = "✽✽✽✽✽"*/
        }
        container.addView(v)
        return v
    }



    override fun getCount(): Int {
        return accountList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}