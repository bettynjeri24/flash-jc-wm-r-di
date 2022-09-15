package io.eclectics.cargilldigital.ui.spinnermgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.FragmentActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.SpinnerLayoutBinding
import io.eclectics.cargilldigital.data.model.FarmerAccount

class BeneficiaryChannelSpinner(context2: FragmentActivity, var outletList: List<FarmerAccount.BeneficiaryAccObj>) : BaseAdapter() {
   lateinit var binding:SpinnerLayoutBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //  var convertView = View.inflate(parent!!.context, R.layout.spinner_layout,null)
        val layoutInflater = LayoutInflater.from(parent!!.context!!)
         binding = SpinnerLayoutBinding.inflate(layoutInflater,parent,false)
        binding.tvProductName.text = "${outletList[position].channelName} "//${outletList[position].accNo}
       // binding.imgProduct.setImageResource(R.mipmap.orangemoney)
        getImageResource(outletList[position].channelName)
        /* var d = RegionSpinnerBinding.
        //var imgProduct = convertView.imgProduct
        var productName = convertView.tvProductName
        // var obj = OutletObjDetail("1","Select Outlet","","","","","","")
        //outletList.add(obj)
        //imgProduct.setImageResource()
        productName.text = outletList[position].regName*/
        return binding.root
    }

    override fun getItem(position: Int): FarmerAccount.BeneficiaryAccObj {
        return outletList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return outletList.size
    }
    private fun getImageResource(channelName: String){
        when(channelName){
            "Orange"->{
                binding.imgProduct.setImageResource(R.mipmap.orangemoney)
            }
            "MTN"->{binding.imgProduct.setImageResource(R.mipmap.mtnmoney)}
            "Orabank"->binding.imgProduct.setImageResource(R.mipmap.orabank)
            "Wave"->binding.imgProduct.setImageResource(R.mipmap.wave)
            "Moov"->binding.imgProduct.setImageResource(R.mipmap.moovafrica)
            else ->{binding.imgProduct.setImageResource(R.mipmap.otp_icon)}
        }
    }
}

