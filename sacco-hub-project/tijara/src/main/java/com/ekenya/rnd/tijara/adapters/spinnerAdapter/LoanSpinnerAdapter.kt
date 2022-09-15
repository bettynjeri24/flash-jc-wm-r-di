package com.ekenya.rnd.tijara.adapters.spinnerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.model.GenderItems
import com.ekenya.rnd.tijara.network.model.LoanProduct
import kotlinx.android.synthetic.main.gender_dropdown_item_list.view.*

class LoanSpinnerAdapter(context: Context, list: List<LoanProduct>) : ArrayAdapter<LoanProduct>(context, 0, list) {
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
        view.tv_gender.text=name!!.name

        return view
    }

}