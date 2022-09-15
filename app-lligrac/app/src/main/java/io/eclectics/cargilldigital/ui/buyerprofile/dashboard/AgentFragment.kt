package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

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

import io.eclectics.cargilldigital.databinding.FragmentAgentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AgentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AgentFragment : Fragment() {

    private var _binding: FragmentAgentBinding? = null
    private val binding get() = _binding!!

    private lateinit var agentNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentBinding.inflate(inflater, container, false)

//        binding.tvCheckFloat.setOnClickListener {
//            show = true
//            animate(container!!, binding)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        agentNavController = Navigation.findNavController(view)

        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.fragment_agent) as NavHostFragment
        agentNavController = nestedNavHostFragment.navController

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

     /*   binding.tvGreetings.text = getString(R.string.greetings, "Afternoon", "Lennox")

        binding.tvCheckFarmers.setOnClickListener {
            if (agentNavController.currentDestination?.id != R.id.checkFarmersFragment2) {
                agentNavController.navigate(R.id.action_checkFloatFragment2_to_checkFarmersFragment2)
            }
        }

        binding.tvCheckFloat.setOnClickListener {
            if (agentNavController.currentDestination?.id != R.id.checkFloatFragment2) {
                agentNavController.navigate(R.id.action_checkFarmersFragment2_to_checkFloatFragment2)
            }
        }*/
    }

}