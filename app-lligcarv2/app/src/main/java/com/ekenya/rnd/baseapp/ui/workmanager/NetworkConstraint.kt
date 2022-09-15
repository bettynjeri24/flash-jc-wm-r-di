package com.ekenya.rnd.baseapp.ui.workmanager

import androidx.work.Constraints
import androidx.work.NetworkType

class NetworkConstraint {

    // Create Network constraint
    var constraints: Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}
