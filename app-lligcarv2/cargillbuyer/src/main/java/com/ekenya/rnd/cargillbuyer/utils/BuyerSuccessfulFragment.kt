package com.ekenya.rnd.cargillbuyer.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.cargillbuyer.R
import com.ekenya.rnd.common.databinding.FragmentSucessfulBinding
import com.ekenya.rnd.common.dialogs.base.adapter_detail.DetailDialogAdapter
import com.ekenya.rnd.common.dialogs.base.adapter_detail.model.DialogDetailCommon
import dagger.android.support.DaggerFragment

class BuyerSuccessfulFragment : DaggerFragment() {

    private lateinit var binding: FragmentSucessfulBinding

    private val mBackStackField by lazy {
        val field = NavController::class.java.getDeclaredField("mBackStack")
        field.isAccessible = true
        field
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSucessfulBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    // set up UI
    private fun setUpUI() {
        binding.apply {
            recyclerViewDetails.apply {
                val dialogAdapter = DetailDialogAdapter()
                val linearLayoutManager = LinearLayoutManager(context)
                layoutManager = linearLayoutManager

                // you should pass a hashmap as a serializable object
                val contentHashMap: HashMap<String, String> =
                    arguments?.getSerializable("content") as HashMap<String, String>
                val contentList: MutableList<DialogDetailCommon> = ArrayList()
                for (key in contentHashMap.keys) {
                    contentList.add(DialogDetailCommon(key, contentHashMap[key].toString()))
                }
                dialogAdapter.submitList(contentList) // Submit a list here
                adapter = dialogAdapter
            }

            buttonContinue.setOnClickListener {
                findNavController().navigate(R.id.action_successfulFragment_to_buyerHomeFragment)
                //  popToRoot(findNavController())
            }

            textViewTitle.text = arguments?.getString("title")
            textViewSubTitle.text = arguments?.getString("subtitle")
            textViewCardTitle.text = arguments?.getString("cardTitle")
            textViewCardContent.text = arguments?.getString("cardContent")
        }
    }

    private fun popToRoot(navController: NavController) {
        val arrayDeque =
            mBackStackField.get(navController) as java.util.ArrayDeque<NavBackStackEntry>
        val graph = arrayDeque.first.destination as NavGraph
        // val rootDestinationId = graph.getStartDestination()
        val rootDestinationId = graph.startDestinationId

        val navOptions = NavOptions.Builder()
            .setPopUpTo(rootDestinationId, false)
            .build()

        navController.navigate(rootDestinationId, null, navOptions)
    }
}
