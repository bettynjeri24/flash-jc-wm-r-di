package com.ekenya.rnd.authcargill.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.ui.login.LoginPinFragment
import com.ekenya.rnd.common.data.network.ResourceNetwork
import com.ekenya.rnd.common.utils.custom.showCargillCustomWarningDialog
import com.ekenya.rnd.common.utils.custom.timber
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

/**
 * handleApiError method to be reused to handle Api Error incase of an error
 */
fun Fragment.handleApiError(
    failure: ResourceNetwork.Failure,
    errMessage: String? = getString(com.ekenya.rnd.common.R.string.invalid_credentials),
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> {
            requireView().showSnackBar(
                getString(com.ekenya.rnd.common.R.string.check_your_internet_connection),
                retry
            )
        }
        failure.errorCode == 401 -> {
            // requireView().showSnakBar("incorrect details")
            if (this is LoginPinFragment) {
                requireView().showSnackBar(getString(com.ekenya.rnd.common.R.string.entered_incorrect_details))
            } else {
                requireView()
                    .showSnackBar(
                        getString(com.ekenya.rnd.common.R.string.we_apologise_for_any_inconvenience)
                    )
            }
        }
        failure.errorCode == 400 -> {
            val error = failure.errorString
            val message = StringBuilder()

            error.let {
                try {
                    timber("{}=======1==========> $error")
                    message.append(JSONObject(error).getString("error_description"))
                    timber("{}=======2==========> ${JSONObject(error)}")
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            timber("{}=======400==========> $message")
            val errorMessage = if (error.isNullOrEmpty()) {
                errMessage
            } else {
                message
            }
            requireActivity().showCargillCustomWarningDialog(
                title = errorMessage.toString(),
                description = errorMessage.toString()
            )
            // basicAlert(errorMessage.toString())
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            val message = if (error.isNullOrEmpty()) {
                resources.getString(com.ekenya.rnd.common.R.string.try_again)
            } else {
                error
            }
            requireView().showSnackBar(message)
        }
    }
}

fun Fragment.handleApiErrorToRouteToUssd(
    failure: ResourceNetwork.Failure,
    handleNetworkFailure: (() -> Unit)? = null,
    handleOtherErrors: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> {
            handleNetworkFailure!!.invoke()
            Timber.e(getString(com.ekenya.rnd.common.R.string.check_your_internet_connection))
        }
        failure.errorCode == 401 -> {
            handleOtherErrors!!.invoke()
            requireView()
                .showSnackBar(
                    getString(com.ekenya.rnd.common.R.string.we_apologise_for_any_inconvenience)
                )
        }
        else -> {
            handleOtherErrors!!.invoke()
            val error = failure.errorBody?.string().toString()
            val message = if (error.isNullOrEmpty()) {
                resources.getString(com.ekenya.rnd.common.R.string.try_again)
            } else {
                error
            }
            requireView().showSnackBar(message)
        }
    }
}

/**
 *SnackBar method to be reused
 */
private fun View.showSnackBar(message: String, action: (() -> Unit)? = null) {
    val sB = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        sB.setAction("Retry") {
            it()
        }
    }
    sB.show()
}
