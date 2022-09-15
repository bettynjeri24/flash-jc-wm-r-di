package com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.cargillbuyer.data.models.PaymentOptions
import com.ekenya.rnd.cargillbuyer.databinding.SpinnerLayoutBuyerBinding

class BuyerPaymentOptionSpinnerAdatper(
    var context2: FragmentActivity,
    var outletList: List<PaymentOptions>
) : BaseAdapter() {
    lateinit var binding: SpinnerLayoutBuyerBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent!!.context!!)
        binding = SpinnerLayoutBuyerBinding.inflate(layoutInflater, parent, false)
        binding.tvProductName.text =
            "${outletList[position].optionName} "
        getImageResource(outletList[position].optionId)

        return binding.root
    }

    override fun getItem(position: Int): PaymentOptions {
        return outletList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return outletList.size
    }

    private fun getImageResource(channelName: Int) {
        binding.imgProduct.apply {
            setColorFilter(
                ContextCompat.getColor(context2, com.ekenya.rnd.common.R.color.primary_green),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            imageTintList =
                ColorStateList.valueOf(resources.getColor(com.ekenya.rnd.common.R.color.primary_green))
            setImageResource(com.ekenya.rnd.common.R.mipmap.otp_icon)
        }
    }
}
