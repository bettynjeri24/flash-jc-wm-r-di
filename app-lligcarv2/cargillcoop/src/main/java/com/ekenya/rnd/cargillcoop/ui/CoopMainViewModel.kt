package com.ekenya.rnd.cargillcoop.ui

import android.app.Application
import com.ekenya.rnd.common.repo.SampleRepository
import com.ekenya.rnd.common.utils.base.viewmodel.BaseViewModel
import javax.inject.Inject

class CoopMainViewModel @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val app: Application
) : BaseViewModel(app)
