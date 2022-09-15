package com.ekenya.rnd.tijara.ui.homepage.services

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.SaccoAccountsAdapter
import com.ekenya.rnd.tijara.adapters.SaccoListAdapter
import com.ekenya.rnd.tijara.databinding.MyAccountsFragmentBinding
import com.ekenya.rnd.tijara.ui.auth.login.LoginPinFragment
import com.ekenya.rnd.tijara.utils.makeGone
import com.ekenya.rnd.tijara.utils.makeVisible
import com.ekenya.rnd.tijara.utils.showToast
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyAccountsFragment : Fragment() {
private lateinit var bindind:MyAccountsFragmentBinding
    private lateinit var viewModel: MyAccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind= MyAccountsFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MyAccountsViewModel::class.java)
        bindind.viewModel=viewModel
        bindind.lifecycleOwner=this
        bindind.whiteBgLoading.makeVisible()

        viewModel.status.observe(viewLifecycleOwner,Observer{
            if (it!==null){
                when(it){
                    1->{
                        bindind.whiteBgLoading.makeGone()
                    }
                    0->{
                        bindind.whiteBgLoading.makeGone()

                    }
                    else->{
                        bindind.whiteBgLoading.makeGone()

                    }
                }
            }
        })
        return bindind.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavUp()
        bindind.apply {
            saccoContinue.setOnClickListener {
                if (Constants.SIGNUPORGID==""){
                    showToast(requireContext(),getString(R.string.select_one_sacco_to_continue))
                }else{
                    /*if (savedInstanceState == null) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.Coontainer, LoginPinFragment.loginInstance())
                            .commitNow()
                    }*/
                }}
            rvSelectSacco.adapter= SaccoAccountsAdapter(SaccoAccountsAdapter.OnClickListener{

            },requireContext())
        }
    }
    private fun setupNavUp() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        bindind.toolbar.custom_toolbar.setupWithNavController(navController, appBarConfiguration)
        bindind.toolbar.custom_toolbar.custom_toolbar_title.text  = "Accounts"
    }

}