package io.eclectics.cargilldigital.adapter


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.AdapterTransactionsBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargill.model.RecentTransaction
import io.eclectics.cargill.utils.NetworkUtility


class TransactionsAdapter (val clickListener: TransactionListener,var data:List<FarmerTransaction>) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    var userJson  = UtilPreference().getUserData(AppCargillDigital.applicationContext().applicationContext)
    var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
     override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userData,clickListener, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var context = AppCargillDigital.applicationContext().applicationContext
        fun bind(
            userData: UserDetailsObj,
            clickListener: TransactionListener,
            item: FarmerTransaction
        ) { // a refactor of onBindViewHolder method

            binding.apply {
                if(item.sendorPhoneNumber.trim().contentEquals(userData.phoneNumber)) {
                    //val drawable = AppCompatResources.getDrawable(context, ic_arrow_up)
                    //val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_up, null)
                    //val img: Drawable = getContext().getResources().getDrawable(R.drawable.smiley)import android.R
                    val imgs: Drawable = context.getResources()
                        .getDrawable(R.drawable.ic_arrow_down)//.setTint(context.resources.getColor(R.color.reddish))
                    imgs.setTint(context.resources.getColor(R.color.reddish))
                    tvTransaction.text = "${item.recipientName}"
                    tvDescription.text = "Account: ${item.sendorPhoneNumber}"
                    tvAmount.apply {

                        setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                        text = "- ${NetworkUtility().cashFormatter(item.amount!!)}"
                        setTextColor(resources.getColor(R.color.reddish))//"CFA ${item.amountValue}"
                    }
                }else{


                    val imgs:Drawable = context.getResources().getDrawable(R.drawable.ic_arrow_up)
                    //txtVw.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                    tvTransaction.text = "${item.sendorName}"
                    tvDescription.text = "Account: ${item.phonenumber}"
                    tvAmount.apply {
                        imgs.setTint(context.resources.getColor(R.color.primary_green))
                        setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                        text = NetworkUtility().cashFormatter(item.amount!!)
                        setTextColor(resources.getColor(R.color.primary_green))//"CFA ${item.amountValue}"
                    }
                }

                tvDate.text = item.datetime
            }

//            if (item.transType == "credit"){
//                binding.tvAmount.setTextColor(R.color.accent2)
//                binding.tvAmount.setCompoundDrawables(R.drawable.ic_arrow_up,0,0,0)
//                binding.tvAmount.compoundDrawableTintMode = R.color.accent2
//            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterTransactionsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

}


class RecentTransactionsAdapter (val clickListener: TransactionListener,var data:List<RecentTransaction>) : RecyclerView.Adapter<RecentTransactionsAdapter.ViewHolder>() {
    var userJson  = UtilPreference().getUserData(AppCargillDigital.applicationContext().applicationContext)
    var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userData, data!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: AdapterTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var context = AppCargillDigital.applicationContext().applicationContext
        fun bind(userData: UserDetailsObj, item: RecentTransaction) { // a refactor of onBindViewHolder method

            binding.apply {
                if(item.sendorPhoneNumber.trim().contentEquals(userData.phoneNumber)) {
                    //val drawable = AppCompatResources.getDrawable(context, ic_arrow_up)
                    //val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_up, null)
                    //val img: Drawable = getContext().getResources().getDrawable(R.drawable.smiley)import android.R
                    val imgs:Drawable = context.getResources().getDrawable(R.drawable.ic_arrow_down)//.setTint(context.resources.getColor(R.color.reddish))
                    imgs.setTint(context.resources.getColor(R.color.reddish))
                    tvTransaction.text = "${item.sendorName}"
                    tvDescription.text = "Account: ${item.sendorPhoneNumber}"
                    tvAmount.apply {

                        setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                        text = "- ${NetworkUtility().cashFormatter(item.amount!!)}"
                        setTextColor(resources.getColor(R.color.reddish))//"CFA ${item.amountValue}"
                    }

                }else{


                    val imgs:Drawable = context.getResources().getDrawable(R.drawable.ic_arrow_up)
                    //txtVw.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                    tvTransaction.text = "${item.recipientName}"
                    tvDescription.text = "Account: ${item.recipientPhoneNumber}"
                    tvAmount.apply {
                        imgs.setTint(context.resources.getColor(R.color.primary_green))
                        setCompoundDrawablesWithIntrinsicBounds(imgs, null, null, null)
                        text = NetworkUtility().cashFormatter(item.amount!!)
                        setTextColor(resources.getColor(R.color.primary_green))//"CFA ${item.amountValue}"
                    }
                }

                tvDate.text = item.datecreated
            }

//            if (item.transType == "credit"){
//                binding.tvAmount.setTextColor(R.color.accent2)
//                binding.tvAmount.setCompoundDrawables(R.drawable.ic_arrow_up,0,0,0)
//                binding.tvAmount.compoundDrawableTintMode = R.color.accent2
//            }
        }

        companion object { //a refactoring of onCreateViewHolder method
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterTransactionsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

}

/**
 * Handles clicks to the layout items
 */
class TransactionListener(val clickListener: (transaction: FarmerTransaction) -> Unit) {
    fun onClick(transaction: FarmerTransaction) = clickListener(transaction)
}