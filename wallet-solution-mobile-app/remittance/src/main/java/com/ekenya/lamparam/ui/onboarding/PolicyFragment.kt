package com.ekenya.lamparam.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentPolicyBinding
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_policy.*
import kotlinx.android.synthetic.main.header_layout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PolicyFragment : Fragment() {

    private var _binding: FragmentPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)

//        ((activity as OnBoardingActivity).hideActionBar())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = resources.getText(R.string.privacy_policy)

        //back button
        ((activity as OnBoardingActivity).onBackClick(btn_back,view))

        binding.btnAcceptTerms.setOnClickListener {
            val bundles = Bundle()
            bundles.putBoolean("accept", true)

            findNavController().navigate(R.id.nav_regPersonalInfo, bundles, null)
        }

    }

}