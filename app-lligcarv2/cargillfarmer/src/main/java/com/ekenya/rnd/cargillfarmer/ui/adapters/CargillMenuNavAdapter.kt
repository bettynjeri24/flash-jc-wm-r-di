package com.ekenya.rnd.cargillfarmer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillfarmer.databinding.ItemWalletAdapterMenusBinding


class CargillMenuNavAdapter(
    private val model: List<CargillMenuModel>,
    private val listener: OnCargillMenuItemClickedListener
) : ListAdapter<CargillMenuModel, CargillMenuNavAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemWalletAdapterMenusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.binding.apply {
            tvMenuName.text = model[position].menuName
            ivMenuIcon.setImageResource(model[position].menuImg)
            cvWalletMenu.setOnClickListener {
                listener.onWalletMenuClicked(it, model = model[position])
            }
        }


    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(val binding: ItemWalletAdapterMenusBinding) :
        RecyclerView.ViewHolder(binding.root)


}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<CargillMenuModel>() {
    override fun areItemsTheSame(
        oldItem: CargillMenuModel,
        newItem: CargillMenuModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CargillMenuModel,
        newItem: CargillMenuModel
    ): Boolean {
        return oldItem == newItem
    }

}


interface OnCargillMenuItemClickedListener {
    fun onWalletMenuClicked(view: View, model: CargillMenuModel)
}


data class CargillMenuModel(
    var menuId: Int,
    var menuName: String = "",
    var menuImg: Int = 0,
) {
    companion object {
        fun getCargillMenuModelModel() = listOf(
            CargillMenuModel(1, "My Accounts", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
            CargillMenuModel(2, "Funds Transfer", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
            CargillMenuModel(3, "Bills", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
            CargillMenuModel(4, "Buy AirTime", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
            CargillMenuModel(5, "Deposit", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
            CargillMenuModel(6, "Withdraw", com.ekenya.rnd.common.R.drawable.ic_bill_payment),
        )


    }
}