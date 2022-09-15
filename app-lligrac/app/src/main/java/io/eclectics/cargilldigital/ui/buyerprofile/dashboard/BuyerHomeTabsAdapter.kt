package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.eclectics.cargilldigital.R

class BuyerHomeTabsAdapter(
    var fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var context: Context?
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = BuyerRecentTranaction()
            1 -> fragment = DashboardFundsRequestList()
        }
        return fragment!!
    }

    private val adaptorItems = 2

    override fun getItemCount(): Int {
        return adaptorItems
    }

    fun getPageTitle(position: Int): CharSequence {
        var title: CharSequence? = null
        //var context = AppCargillDigital.applicationContext()
        when (position) {
            0 -> title = context!!.resources.getString(R.string.dashboard_transaction)
            1 -> title = context!!.resources.getString(R.string.funds_request_made)

        }
        return title!!
    }
}
