package io.eclectics.cargilldigital.ui.generalwalletprofile.statement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AccountsTabsAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MiniFragment()
            1 -> fragment = FullStatement()
        }
        return fragment!!
    }

    private val adaptorItems = 2

    override fun getItemCount(): Int {
        return adaptorItems
    }

    fun getPageTitle(position: Int): CharSequence {
        var title: CharSequence? = null
        when (position) {
            0 -> title = "Mini Statement"
            1 -> title = "Full Statement"

        }
        return title!!
    }
}
