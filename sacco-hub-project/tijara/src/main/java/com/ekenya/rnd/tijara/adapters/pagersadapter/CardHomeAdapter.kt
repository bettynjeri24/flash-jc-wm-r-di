package com.ekenya.rnd.tijara.adapters.pagersadapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.AccountsCallBack

class CardHomeAdapter(val context: Context,
                      private val accountList: List<Accounts>,private val acountCallBack:AccountsCallBack): PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //pick single item within the list
        val myAccunt: Accounts =accountList[position]
        var v = View((context as AppCompatActivity).applicationContext)
        v = LayoutInflater.from(container.context).inflate(
            R.layout.home_card_row,
            container,
            false
        )

        v.findViewById<Button>(R.id.btnViewBal).text=if (myAccunt.showBalance){
            v.context.getText(R.string.hide_balance)
        }
        else{
            v.context.getText(R.string.view_balance)
        }
        v.findViewById<Button>(R.id.btnViewBal).setOnClickListener {
            Log.d("TAG","CARD HOME")
            Constants.isMobileMClicked=true
            Constants.isMore=false
            acountCallBack.onItemSelected(myAccunt,position)
        }
        v.findViewById<Button>(R.id.btn_state).setOnClickListener {
            Constants.isMobileMClicked=false
            Constants.isMore=true
            Log.d("TAG","HOME")
            acountCallBack.onItemSelected(myAccunt,position)
        }

        if (myAccunt.isShare==0) {
            val accNumber=myAccunt.accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
            (v.findViewById<View>(R.id.tv_accountNumber) as TextView).text = accNumber
            (v.findViewById<View>(R.id.textacName) as TextView).text = myAccunt.accountName
            toggleSavingAccountsVisibility(v,myAccunt)
        }else{
            val shareNumber=myAccunt.accountNumber.replace("(?<=.{3}).(?=.{3})".toRegex(),"*")
            (v.findViewById<View>(R.id.tv_accountNumber) as TextView).text = shareNumber
            (v.findViewById<View>(R.id.textacName) as TextView).text = myAccunt.product
            toggleShareAccountsVisibility(v,myAccunt)

            (v.findViewById<View>(R.id.tvAvailBal) as TextView).text = "Share Capital:"
            (v.findViewById<View>(R.id.tvActulBal) as TextView).text = "Dividend"

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
    private fun toggleSavingAccountsVisibility(view: View,myAccunt:Accounts){
        if(myAccunt.showBalance){
            (view.findViewById<View>(R.id.tvAvailBalValue) as TextView).text = "${myAccunt.defaultCurrency}  ${myAccunt.availableBalance}"
            (view.findViewById<View>(R.id.tvAcBalValue) as TextView).text = "${myAccunt.defaultCurrency}  ${myAccunt.currentBalance}"
        }else{
            (view.findViewById<View>(R.id.tvAvailBalValue) as TextView).text ="✽✽✽✽✽"
            (view.findViewById<View>(R.id.tvAcBalValue) as TextView).text = "✽✽✽✽✽"
        }
    }
    private fun toggleShareAccountsVisibility(view: View,myAccunt:Accounts){
        if (myAccunt.showBalance){
            (view.findViewById<View>(R.id.tvAvailBalValue) as TextView).text = "${myAccunt.defaultCurrency} ${myAccunt.shareCapital.toString()}"
            (view.findViewById<View>(R.id.tvAcBalValue) as TextView).text = "${myAccunt.defaultCurrency} ${myAccunt.dividend.toString()}"
        }else{
            (view.findViewById<View>(R.id.tvAvailBalValue) as TextView).text ="✽✽✽✽✽"
            (view.findViewById<View>(R.id.tvAcBalValue) as TextView).text = "✽✽✽✽✽"
        }
    }
    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}