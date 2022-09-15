package io.eclectics.cargilldigital.ui.cooperativeprofile.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentCooperativeBinding

@AndroidEntryPoint
class CooperativeFragment : Fragment() {
    private var _binding: FragmentCooperativeBinding? = null
    private val binding get() = _binding!!

    private lateinit var coopNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCooperativeBinding.inflate(inflater, container, false)

//        binding.tvCheckFloat.setOnClickListener {
//            show = true
//            animate(container!!, binding)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coopNavController = Navigation.findNavController(view)

        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.fragment_agent) as NavHostFragment
        coopNavController = nestedNavHostFragment.navController

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*   binding.tvGreetings.text = getString(R.string.greetings, "Afternoon", "Lennox")

           binding.tvCheckFarmers.setOnClickListener {
               if (coopNavController.currentDestination?.id != R.id.checkFarmersFragment2) {
                   coopNavController.navigate(R.id.action_checkFloatFragment2_to_checkFarmersFragment2)
               }
           }

           binding.tvCheckFloat.setOnClickListener {
               if (coopNavController.currentDestination?.id != R.id.checkFloatFragment2) {
                   coopNavController.navigate(R.id.action_checkFarmersFragment2_to_checkFloatFragment2)
               }
           }*/
    }

}