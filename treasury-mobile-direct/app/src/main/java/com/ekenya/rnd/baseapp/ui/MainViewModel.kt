package com.ekenya.rnd.baseapp.ui

import androidx.lifecycle.AndroidViewModel
import com.ekenya.rnd.baseapp.TMDApp
import com.ekenya.rnd.common.repo.SampleRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val app: TMDApp
) : AndroidViewModel(app) {

    fun getData(): String {
        return sampleRepository.data
    }
}
