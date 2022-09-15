package com.ekenya.lamparam.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Control SplashFragment UI
 */
class SplashViewModel: ViewModel() {

    /**
     * Navigate to Check navigated Fragment
     */
    private val _eventTimeExpired = MutableLiveData<Boolean>()
    val eventTimeExpired: LiveData<Boolean>
        get() = _eventTimeExpired

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()

    init {
        //initialise the values at first so that no navigation can occur
        _eventTimeExpired.value = false

        // Creates a timer which triggers the NAVIGATION OF SCREEN when it finishes
        timer = object : CountDownTimer(SPLASH_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventTimeExpired.value = true
            }
        }

        timer.start()
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        _eventTimeExpired.value = false
    }

    /** Methods for completed events **/
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game
        const val SPLASH_TIME = 2000L
    }

}