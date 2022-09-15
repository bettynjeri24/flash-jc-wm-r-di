package io.eclectics.cargilldigital.utils.dk

import java.util.ArrayList


var USSDSHORTCODE = "747"
var USSD_RESPONSE = ""
var PIN = ""
var PHONE_NUMBER = ""
var AUTH_TOKEN = ""
var PHONENUMBER: String = ""
var FFUNIXTIMESTAMP: Long = 0L

var SECRET_KEY: String = ""


/**
 * WORK MANAGER CONSTANTS
 */
// Name of Notification Channel for verbose notifications of background work
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
const val KEY_OUTPUT_DATA = "KEY_OUTPUT_DATA"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

// The name of the Sync Data work
const val SYNC_DATA_WORK_NAME = "sync_data_work_name"


// Other keys
const val DELAY_TIME_MILLIS: Long = 3000

const val TAG_SYNC_DATA = "TAG_SYNC_DATA"

/**
 *  END
 *  */

//
lateinit var SIM_MAP: MutableMap<String, Int>
var SIMCARDNAMES: ArrayList<String>? = null


var CURRENT_USER_PHONENUMBER: String = ""
var CARGILL_USER_ID: String = ""
var CARGILL_USERINDEX: String = ""
var CARGILL_COOPERATIVEID: String = ""
var CARGILL_COOPERATIVEINDEX: String = ""
var CARGILL_FULL_NAME: String = ""
var CARGILL_PROVIDEDUSERID: String = ""
var CARGILL_USER_NAME: String = ""
var CARGILL_SECTION: String = ""