package com.ekenya.rnd.cargillfarmer.ui.adapters.spinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ekenya.rnd.cargillfarmer.databinding.ItemCustomSpinnerBinding
import com.ekenya.rnd.cargillfarmer.utils.getImageResource
import com.ekenya.rnd.common.data.db.entity.CargillUserChannelData


class CustomArrayAdapter(context: Context, var dataSource: List<CargillUserChannelData>) :
    ArrayAdapter<CargillUserChannelData>(context, 0, dataSource) {
    private lateinit var binding: ItemCustomSpinnerBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            binding =
                ItemCustomSpinnerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            binding.tvServiceProvider.setText(dataSource[position].channelName.toString())
            binding.ivServiceProvider.setBackgroundResource(getImageResource(dataSource[position].channelName.toString()))

        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            binding =
                ItemCustomSpinnerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            binding.tvServiceProvider.setText(dataSource[position].channelName.toString())
            binding.ivServiceProvider.setBackgroundResource(getImageResource(dataSource[position].channelName.toString()))


        }
        return binding.root
    }


}

