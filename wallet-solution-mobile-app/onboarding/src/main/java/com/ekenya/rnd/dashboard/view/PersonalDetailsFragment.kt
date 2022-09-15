package com.ekenya.rnd.dashboard.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.databinding.FragmentPersonalDetailsBinding


class PersonalDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPersonalDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalDetailsBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.tvIDNumberValue.text = SharedPreferencesManager.getIDNumber(requireContext())
        binding.tvDobValue.text = SharedPreferencesManager.getDob(requireContext())
        val lastName = SharedPreferencesManager.getLastName(requireContext())
        val secondName = SharedPreferencesManager.getMiddleName(requireContext())
        val firstName = SharedPreferencesManager.getFirstName(requireContext())
        binding.tvFirstNameValue.text ="$firstName $secondName $lastName"
        binding.userLegalName.text ="$firstName $secondName $lastName"

        setProfileImage()

    }

    private fun setProfileImage() {

        val imageString = SharedPreferencesManager.getProfilePhoto(requireContext())
        try {

            val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.userImage.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }


    }

}