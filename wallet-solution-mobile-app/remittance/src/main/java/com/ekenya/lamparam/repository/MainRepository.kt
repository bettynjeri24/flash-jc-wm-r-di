package com.ekenya.lamparam.repository

import com.ekenya.lamparam.network.LamparamApi
import javax.inject.Inject

/**
 * Used by all fragments in MainActivity
 */
class MainRepository @Inject constructor(private val api: LamparamApi) {
}