package com.ekenya.lamparam.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ekenya.lamparam.R
import kotlinx.android.synthetic.main.fragment_add_viewpg.*
import kotlinx.android.synthetic.main.fragment_add_viewpg.view.*
import java.util.*


class FragmentAddViewpg : Fragment() {

    lateinit var viewPager: ViewPager
    lateinit var myViewPagerAdapter: MyViewPagerAdapter
    //var layouts by Delegates.notNull<Int>()[]
    lateinit var layouts:IntArray
    //var page by Delegates.notNull<Int>()
    var page = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_add_viewpg, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayoutDots.setupWithViewPager(view_pager)
        viewPager= view.view_pager
        layouts = intArrayOf(
            R.layout.layout_add1,
            R.layout.layout_add1,
            R.layout.layout_add1
        )
        //page =0
        myViewPagerAdapter =  MyViewPagerAdapter(requireActivity());
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        pageSwitcher(4,requireActivity())
    }

    inner class MyViewPagerAdapter(var activity: FragmentActivity)
    /**
     * Instantiates a new My view pager adapter.
     */
        : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

            val view: View = layoutInflater!!.inflate(layouts.get(position), container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

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
     * The Page.
     */


    /**
     * Page switcher.
     *
     * @param seconds the seconds
     */
    fun pageSwitcher(seconds: Int, activity: FragmentActivity) {
        timer = Timer() // At this line a new Thread will be created
        timer!!.scheduleAtFixedRate(RemindTask(activity), 0, seconds * 1000.toLong()) // delay
        // in
        // milliseconds
    }

    /**
     * The View pager page change listener.
     */
    //  viewpager change listener
    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
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
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            activity.runOnUiThread(Runnable {
                if (page > 3) {
                    // In my case the number of pages are 5
                    viewPager.currentItem = 0
                    timer!!.cancel()
                    // Showing a toast for just testing purpose
                    /*      Toast.makeText(getApplicationContext(), "Timer stoped",
                          Toast.LENGTH_LONG).show();*/
                    /*  viewPager.setCurrentItem(0);*/
                } else {
                    viewPager.setCurrentItem(page++)

                }
            })
        }
    }

}