package io.eclectics.cargilldigital.ui.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Control LanguageFragment UI
 */
@HiltViewModel
class LanguageViewModel @Inject constructor() : ViewModel() {
    /**
     * Navigate to Check navigated Fragment
     */
    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    init {
        //initialise the values at first so that no navigation can occur
        _navigate.value = false
    }

    /**
     * Show that the navigation has occurred
     */
    fun navigate() {
        _navigate.value = true
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        _navigate.value = false
    }

    /** Methods for completed events **/
    override fun onCleared() {
        super.onCleared()
        _navigate.value = false
    }

}