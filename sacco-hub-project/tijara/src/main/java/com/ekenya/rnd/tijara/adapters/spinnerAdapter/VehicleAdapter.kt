package com.ekenya.rnd.tijara.adapters.spinnerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.VehiclesTypes
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.camelCase
import kotlinx.android.synthetic.main.gender_dropdown_item_list.view.*

class VehicleAdapter(context: Context, list: List<VehiclesTypes>) : ArrayAdapter<VehiclesTypes>(context, 0, list) {
    var name1=""
    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }
    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }
    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val name = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.gender_dropdown_item_list,
            parent,
            false
        )
        val formatNmae= name?.name?.let { camelCase(it) }
        view.tv_gender.text= formatNmae

        return view
    }

}