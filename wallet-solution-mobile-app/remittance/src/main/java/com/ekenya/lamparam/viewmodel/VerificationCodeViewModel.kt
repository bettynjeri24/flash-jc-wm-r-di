package com.ekenya.lamparam.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerificationCodeViewModel : ViewModel() {

    /**
     * Navigate to Check navigated Fragment
     */
    private val _navigated = MutableLiveData<Boolean>()
    val navigated: LiveData<Boolean>
        get() = _navigated

    /**
     * Timer to be observed
     */
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val timer: CountDownTimer

    init {
        //initialise the values at first so that no navigation can occur
        _navigated.value = false

        // Creates a timer which triggers the CHANGE OF ONBOARDING CONTENT when it finishes
        timer = object : CountDownTimer(
            WAIT_TIME,
            ONE_SECOND
        ) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
            }
        }

        timer.start()
    }

    fun navigate() {
        _navigated.value = true
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        _navigated.value = false
        timer.cancel()
    }

    /** Methods for completed events **/
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        _navigated.value = false
    }

    fun refreshTimer() {
        timer.start()
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of a single onboarding page
        const val WAIT_TIME = 60000L
    }
}
