package com.ekenya.rnd.cargillfarmer.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.cargillfarmer.databinding.RowAccountDetailsBinding

class MyViewPagerAdapter(private val accountDetails: List<String>) :
    RecyclerView.Adapter<MyViewPagerAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(val binding: RowAccountDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = RowAccountDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = accountDetails.size
}