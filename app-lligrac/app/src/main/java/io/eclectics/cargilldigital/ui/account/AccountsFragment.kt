package io.eclectics.cargilldigital.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.agritech.cargill.ui.account.AccountsViewModel
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.AccountsFragmentBinding
import io.eclectics.cargilldigital.utils.UtilPreference
import javax.inject.Inject

@AndroidEntryPoint
class AccountsFragment : Fragment() {
    @Inject
    lateinit var navoption: NavOptions
    val viewModel: AccountsViewModel by viewModels()

    private var _binding: AccountsFragmentBinding? = null
    private val binding get() = _binding!!

    private var account = "agent"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AccountsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it)
                navigate()
        })

        //binding.btnProceedAccount.setOnClickListener { viewModel.navigate() }

        binding.cvAgent.setOnClickListener {
            account = "agent"
            binding.ivFarmer.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivAgent.setImageDrawable(resources.getDrawable(R.drawable.radio_selected))
            findNavController().navigate(R.id.nav_agentProfile,null,navoption)
        }

        binding.cvFarmer.setOnClickListener {
            account = "farmer"
            binding.ivAgent.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivFarmer.setImageDrawable(resources.getDrawable(R.drawable.radio_selected))
            binding.ivAgrovet.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivFarmer2.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            var bundle = Bundle()
            bundle.putString("menu","mainmenu")
            findNavController().navigate(R.id.nav_farmerDashboard,bundle, navoption)
        } //cv_farmSupervisor   cv_agrovet
        binding.cvCooperative.setOnClickListener {
            account = "cooperative"
            binding.ivAgent.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivFarmer.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivAgrovet.setImageDrawable(resources.getDrawable(R.drawable.radio_selected))
            binding.ivFarmer2.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            findNavController().navigate(R.id.nav_cooperativeProfile,null, navoption)
        }
        binding.cvGeneralWallet.setOnClickListener {
            account = "generalwallet"
            binding.ivAgent.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivFarmer.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivAgrovet.setImageDrawable(resources.getDrawable(R.drawable.radio_normal))
            binding.ivFarmer2.setImageDrawable(resources.getDrawable(R.drawable.radio_selected))
            //sset float balance dummy
            UtilPreference().saveFloatBalance(requireActivity(), "50000")
            findNavController().navigate(R.id.nav_generalWalletProfile,null, navoption)

        }
    }

    private fun navigate(){
        val bundle = Bundle()
        bundle.putString("accountType", account)
//        view?.findNavController()?.navigate(R.id.action_accountsFragment_to_loginFragment)
        findNavController().navigate(R.id.nav_loginFragment,bundle, null)
        viewModel.hasNavigated()
    }
}