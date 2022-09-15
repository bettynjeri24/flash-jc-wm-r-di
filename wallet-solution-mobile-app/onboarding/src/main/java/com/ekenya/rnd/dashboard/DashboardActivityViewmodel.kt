package com.ekenya.rnd.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ekenya.rnd.common.repo.SampleRepository
import javax.inject.Inject

class DashboardActivityViewmodel @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val app: Application
) : AndroidViewModel(app) {

    fun getData(): String {
        return sampleRepository.getData()
    }}