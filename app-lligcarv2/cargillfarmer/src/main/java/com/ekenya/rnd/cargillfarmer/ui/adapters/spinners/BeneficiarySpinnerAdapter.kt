package com.ekenya.rnd.cargillfarmer.ui.adapters.spinners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ekenya.rnd.cargillfarmer.databinding.SpinnerLayoutBinding
import com.ekenya.rnd.common.data.db.entity.CargillUserChannelData

class BeneficiarySpinnerAdapterr(var outletList: List<CargillUserChannelData>) :
    BaseAdapter() {
    lateinit var binding: SpinnerLayoutBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //  var convertView = View.inflate(parent!!.context, R.layout.spinner_layout,null)
        val layoutInflater = LayoutInflater.from(parent!!.context!!)
        binding = SpinnerLayoutBinding.inflate(layoutInflater, parent, false)
        binding.tvProductName.text =
            "${outletList[position].channelName} "
        getImageResource(outletList[position].channelName.toString())

        return binding.root
    }

    override fun getItem(position: Int): CargillUserChannelData {
        return outletList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return outletList.size
    }

    private fun getImageResource(channelName: String) {
        when (channelName) {
            "Orange" -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.orangemoney)
            }
            "MTN" -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.mtnmoney)
            }
            "Orabank" -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.orabank)
            }
            "Wave" -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.wave)
            }
            "Moov" -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.moovafrica)
            }
            else -> {
                binding.imgProduct.setImageResource(com.ekenya.rnd.common.R.mipmap.otp_icon)
            }
        }

    }
}
