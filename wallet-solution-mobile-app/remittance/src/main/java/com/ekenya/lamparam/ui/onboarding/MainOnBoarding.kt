package com.ekenya.lamparam.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentMainOnboardingBinding
import java.util.*

class MainOnBoarding : Fragment() {

    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    lateinit var layouts: IntArray
    var page = 0

    private var _binding: FragmentMainOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentMainOnboardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayoutDots.setupWithViewPager(binding.viewPager)
        layouts = intArrayOf(R.layout.onboarding_one, R.layout.onboarding_two, R.layout.onboarding_three)
        myViewPagerAdapter = MyViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = myViewPagerAdapter
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        pageSwitcher(3, requireActivity())

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.nav_createAccount)
//            findNavController().navigate(R.id.nav_pin)
        }
    }

    /**
     * Instantiates a new My view pager adapter.
     */
    inner class MyViewPagerAdapter(var activity: FragmentActivity) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

            val view: View = layoutInflater!!.inflate(layouts.get(position), container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int = layouts.size

        override fun isViewFromObject(view: View, obj: Any): Boolean = view === obj

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    /**
     * The Timer.
     */
    var timer: Timer? = null

    /**
     * Page switcher.
     *
     * @param seconds the seconds
     */
    fun pageSwitcher(seconds: Int, activity: FragmentActivity) {
        timer = Timer() // At this line a new Thread will be created
        timer!!.scheduleAtFixedRate(RemindTask(activity), 0, seconds * 1000.toLong()) // delay
        // in milliseconds
    }

    /**
     * The View pager page change listener.
     */
    //  viewpager change listener
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {}
        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * The type Remind task.
     */
    // this is an inner class...
    inner class RemindTask(var activity: FragmentActivity) : TimerTask() {
        var page = 0
        override fun run() {
            // As the TimerTask run on a separate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            activity.runOnUiThread(Runnable {
                if (page > 3) {
                    // In my case the number of pages are 5
                    binding.viewPager.currentItem = 0
                    timer!!.cancel()
                } else {
                    binding.viewPager.currentItem = page++
                }
            })
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }

}