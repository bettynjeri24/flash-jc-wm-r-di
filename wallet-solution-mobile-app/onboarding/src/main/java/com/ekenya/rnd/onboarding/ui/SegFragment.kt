package com.ekenya.rnd.onboarding.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.SegFragmentBinding
import jp.shts.android.storiesprogressview.StoriesProgressView


class SegFragment : Fragment(), StoriesProgressView.StoriesListener {

    private val resources = AppUtils.getSliderImages()
    private var bgimg = AppUtils.getBackGroundImages()
    private lateinit var  view: SegFragmentBinding


    private lateinit var fragmentTitles: Array<String>

    private  val headings = AppUtils.getHeadings()
    private val subhdgs = AppUtils.getSubHeadings()

    // on below line we are creating variable for
    // our press time and time limit to display a story.
   private  var pressTime = 0L
    private var limit = 500L

    // on below line we are creating variables for
    // our progress bar view and image view .
    private var storiesProgressView: StoriesProgressView? = null
    private var bg: ConstraintLayout? = null
    private var image: ImageView? = null
    private var onboardingHeadertitle: TextView? = null
    private var fragmentTitle: TextView? = null
    private var onboardingSubheader: TextView? = null

    // on below line we are creating a counter
    // for keeping count of our stories.
    private var counter = 0

    // on below line we are creating a new method for adding touch listener

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event -> // inside on touch method we are
        // getting action on below line.
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                // on action down when we press our screen
                // the story will pause for specific time.
                pressTime = System.currentTimeMillis()

                // on below line we are pausing our indicator.
                storiesProgressView!!.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {

                // in action up case when user do not touches
                // screen this method will skip to next image.
                val now = System.currentTimeMillis()

                // on below line we are resuming our progress bar for status.
                storiesProgressView!!.resume()

                // on below line we are returning if the limit < now - presstime
                return@OnTouchListener limit < now - pressTime
            }
        }
        false

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  view = SegFragmentBinding.inflate(layoutInflater,container,false)

        val view = inflater.inflate(R.layout.seg_fragment, container, false)
        fragmentTitles = requireContext().resources.getStringArray(R.array.fragment_titles)

        storiesProgressView = view.findViewById<View>(R.id.stories) as StoriesProgressView
        bg = view.findViewById<View>(R.id.main_bg) as ConstraintLayout
        onboardingHeadertitle = view.findViewById<TextView>(R.id.tv_onboardingHeadertitle)
        fragmentTitle = view.findViewById<TextView>(R.id.tv_WelcomeTxt)
        onboardingSubheader = view.findViewById<TextView>(R.id.tv_onboardingSubheader)

        storiesProgressView!!.setStoriesCount(resources.size)

        storiesProgressView!!.setStoryDuration(6000L)


        storiesProgressView!!.setStoriesListener(this)

        storiesProgressView!!.startStories(counter)

        image = view.findViewById<View>(R.id.image) as ImageView

        image!!.setImageResource(resources[counter])
        bg!!.setBackgroundResource(bgimg[counter])
        onboardingHeadertitle!!.setText(headings[counter])
        onboardingSubheader!!.setText(subhdgs[counter])
        fragmentTitle!!.setText(fragmentTitles[counter])

        val reverse = view.findViewById<View>(R.id.reverse)
        val btn_getStarted = view.findViewById<Button>(R.id.btn_getStartedo)
        btn_getStarted.setOnClickListener {
            SharedPreferencesManager.setHasFinishedSliders(requireContext(), true)

            findNavController().navigate(R.id.action_segFragment_to_accountLookUpFragment)


        }

        reverse.setOnClickListener { // inside on click we are
            // reversing our progress view.
            storiesProgressView!!.reverse()
        }


        reverse.setOnTouchListener(onTouchListener)


        val skip = view.findViewById<View>(R.id.skip)
        skip.setOnClickListener {
            storiesProgressView!!.skip()
        }

        skip.setOnTouchListener(onTouchListener)


        return view
    }

    override fun onNext() {
        // this method is called when we move
        // to next progress view of story.

        image!!.setImageResource(resources[++counter])
        bg!!.setBackgroundResource(bgimg[counter])
        onboardingHeadertitle!!.text = headings[counter]
        onboardingSubheader!!.text = subhdgs[counter]
        fragmentTitle!!.text = fragmentTitles[counter]


    }

    override fun onPrev() {

        // this method id called when we move to previous story.
        // on below line we are decreasing our counter
        if (counter - 1 < 0) return

        image!!.setImageResource(resources[--counter])
        bg!!.setBackgroundResource(bgimg[counter])
        onboardingSubheader!!.text = subhdgs[counter]
        fragmentTitle!!.text = fragmentTitles[counter]


    }

    override fun onComplete() {
        SharedPreferencesManager.setHasFinishedSliders(requireContext(), true)
        findNavController().navigate(R.id.action_segFragment_to_accountLookUpFragment)
    }

    override fun onDestroy() {
        storiesProgressView!!.destroy()
        super.onDestroy()
    }



  /*  override fun onResume() {
        super.onResume()
        hideSupportActionBar()
        extendStatusBarBackground()

    }

    override fun onStop() {
        super.onStop()

        makeStatusBarTransparent()

    }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSupportActionBar()
        extendStatusBarBackground()

        if (SharedPreferencesManager.hasFinishedSliders(requireContext()) == true && SharedPreferencesManager.hasReachedHomepage(
                requireContext()
            ) == true
        ) {
            //go to get started landing page
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        } else if (SharedPreferencesManager.hasFinishedSliders(requireContext()) == true) {
            findNavController().navigate(R.id.action_segFragment_to_getStartedFragment)

        }

    }


}