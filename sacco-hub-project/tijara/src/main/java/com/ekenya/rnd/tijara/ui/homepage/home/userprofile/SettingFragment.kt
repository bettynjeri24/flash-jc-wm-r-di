package com.ekenya.rnd.tijara.ui.homepage.home.userprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.OnBoardingActivity
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.layoutAdapter.SettingsAdapter
import com.ekenya.rnd.tijara.adapters.layoutAdapter.UserProfileAdapter
import com.ekenya.rnd.tijara.databinding.FragmentSettingBinding
import com.ekenya.rnd.tijara.network.model.BillPaymentMerchant
import com.ekenya.rnd.tijara.ui.homepage.home.callbacks.BillPaymentMerchantCallBack
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class SettingFragment : Fragment(),BillPaymentMerchantCallBack {
    private  var lists:ArrayList<BillPaymentMerchant> = ArrayList()
    private lateinit var binding:FragmentSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingBinding.inflate(layoutInflater)
        setupNavUp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            binding.rvSetting.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            val settingsAdapter= SettingsAdapter(lists,this@SettingFragment)
            binding.rvSetting.adapter=settingsAdapter



        }
        addSettingItems()
    }

    private fun addSettingItems() {
     //   lists.add(BillPaymentMerchant(R.drawable.help,"Help", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.aboutus,"About Us", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.rate_us,"Rate Us", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.share,"Share", R.drawable.arrow_right))
        lists.add(BillPaymentMerchant(R.drawable.logout,"Logout", R.drawable.arrow_right))

    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.custom_toolbar.custom_toolbar_title.text  = getString(R.string.settings)
    }
    private fun shareLink() {
      //  val user=PrefUtils.getPreferences(requireContext(),"firstName")
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, found this cool TIJARA application(${getString(R.string.app_name)}). Kindly check it out by clicking this link to download ${Constants.ApplinkTest}.")
        startActivity(Intent.createChooser(shareIntent, "Share "+getString(R.string.app_name)+" using"))
    }

    override fun onItemSelected(cont: BillPaymentMerchant, pos: Int) {
        when(pos){
            0 -> {
            }
            1 -> {
            }
            2 ->{
                shareLink()
            }
            3 ->{
                Constants.token=""
                Constants.isFromUpdate=0
                val intent = Intent(requireContext(), OnBoardingActivity::class.java)
                startActivity(intent)
            }

        }
    }


}