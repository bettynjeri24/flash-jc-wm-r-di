package com.ekenya.rnd.tmd.ui.fragments.ministatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.mycards.databinding.FragmentMiniStatementBinding

class MiniStatementFragment : Fragment() {

    private lateinit var binding: FragmentMiniStatementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentMiniStatementBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {
            val adapters = AdapterMiniStatement()
            binding.recyclerView.apply {
                adapter = adapters
                layoutManager = LinearLayoutManager(requireContext())
            }
            adapters.submitList(statements)
        }
    }

    val statements = mutableListOf<Statement>().apply {
        add(Statement("POS - Tunnel Shop", "28/01/2022 12:35pm", "- Kes 2,000.00"))
        add(Statement("Card Top-Up", "28/01/2022 12:35pm", "+ Kes 8,000.00"))
        add(Statement("POS - Tunnel Shop", "28/01/2022 12:35pm", "- Kes 2,000.00"))
        add(Statement("Card Top-Up", "28/01/2022 12:35pm", "+ Kes 8,000.00"))
    }
}
