package com.ekenya.rnd.tmd.ui.fragments.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.mycards.databinding.FragmentPersonalBinding
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel
import com.ekenya.rnd.tmd.utils.setUpSpinner

class PersonalFragment(private val viewmodel: OnboardViewModel, val next: () -> Unit) : Fragment() {

    private lateinit var binding: FragmentPersonalBinding
    private var newUserRequest = viewmodel.newUserRequest.value!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentPersonalBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        viewmodel.newUserRequest.observe(viewLifecycleOwner) {
            newUserRequest = it
        }
    }

    private fun setUpUi() {
        binding.apply {
            autocompleteUsCitizen.setUpSpinner(mutableListOf("Yes", "No"), onItemClick = { parent, view, position, id -> })

            textInputEditTextGender.setUpSpinner(mutableListOf("Male", "Female"), onItemClick = { parent, view, position, id -> })

            button3.setOnClickListener {
                newUserRequest.firstName = binding.textInputEditTexFirstName.text.toString()
                newUserRequest.lastName = binding.textInputEditTextLastName.text.toString()
                newUserRequest.phoneNumber = binding.textInputEditTextPhoneNumber.text.toString()
                newUserRequest.email = binding.textInputEditTextEmailAddress.text.toString()
                newUserRequest.dateOfBirth = binding.textInputEditTextDateOfBirth.text.toString()
                newUserRequest.gender = binding.textInputEditTextGender.text.toString()
                viewmodel.newUserRequest.value = newUserRequest
                next()
            }
        }
    }
}
