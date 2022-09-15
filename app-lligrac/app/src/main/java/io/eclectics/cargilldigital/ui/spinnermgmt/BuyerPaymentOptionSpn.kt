package io.eclectics.cargilldigital.ui.spinnermgmt

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.SpinnerLayoutBinding
import io.eclectics.cargilldigital.data.model.BuyerAccount

class BuyerPaymentOptionSpn (var context2: FragmentActivity, var outletList:List<BuyerAccount.PaymentOptions>) : BaseAdapter() {
    lateinit var binding:SpinnerLayoutBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //  var convertView = View.inflate(parent!!.context, R.layout.spinner_layout,null)
        val layoutInflater = LayoutInflater.from(parent!!.context!!)
         binding = SpinnerLayoutBinding.inflate(layoutInflater,parent,false)
        binding.tvProductName.text = "${outletList[position].optionName} "//${outletList[position].accNo}
       // binding.imgProduct.visibility = View.GONE
        getImageResource(outletList[position].optionId)
        /* var d = RegionSpinnerBinding.
        //var imgProduct = convertView.imgProduct
        var productName = convertView.tvProductName
        // var obj = OutletObjDetail("1","Select Outlet","","","","","","")
        //outletList.add(obj)
        //imgProduct.setImageResource()
        productName.text = outletList[position].regName*/
        return binding.root
    }

    override fun getItem(position: Int): BuyerAccount.PaymentOptions {
        return outletList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return outletList.size
    }

    private fun getImageResource(channelName: Int){
        binding.imgProduct.apply {

            setColorFilter(ContextCompat.getColor(context2, R.color.primary_green), android.graphics.PorterDuff.Mode.MULTIPLY)
          //setImageTintList(imageView, ColorStateList.valueOf(yourTint));
            imageTintList = ColorStateList.valueOf(resources.getColor(R.color.primary_green))
            setImageResource(R.mipmap.otp_icon)
        }

            /* when(channelName){

                 1->{
                     binding.imgProduct.setImageResource(R.mipmap.payloans)
                 }
                 2->{binding.imgProduct.setImageResource(R.mipmap.fundrequest)}
                3->binding.imgProduct.setImageResource(R.mipmap.pay_farmer)
            }*/
        }

}
