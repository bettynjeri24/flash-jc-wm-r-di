package io.eclectics.cargilldigital.utils.dk

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.utils.sweetAlertDialogSuccessType
import io.eclectics.cargilldigital.utils.sweetAlertDialogWarningType

fun Fragment.errorNetworkConnectionFailed(
    messageToShow: String = getString(R.string.error_network_connection_failed),
    messageToIntent: String = getString(R.string.error_network_connection_failed)
) {
    requireActivity().sweetAlertDialogWarningType(
        messageToShow.toString(),
        action = {
            val intent = Intent()
            intent.putExtra(getString(R.string.status_code_message_to_farmforce), messageToIntent)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        })
}

fun Fragment.errorBuyerAccountDoesntExist(
    messageToShow: String = getString(R.string.error_buyer_account_doesnt_exist),
    messageToIntent: String = getString(R.string.error_buyer_account_doesnt_exist)
) {
    requireActivity().sweetAlertDialogWarningType(
        messageToShow.toString(),
        action = {
            val intent = Intent()
            intent.putExtra(getString(R.string.status_code_message_to_farmforce), messageToIntent)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        })
}

fun Fragment.errorTransactionCanceled(
    messageToShow: String = getString(R.string.error_transcation_canceled),
    messageToIntent: String = getString(R.string.error_transcation_canceled)
) {
    requireActivity().sweetAlertDialogWarningType(
        messageToShow.toString(),
        action = {
            val intent = Intent()
            intent.putExtra(getString(R.string.status_code_message_to_farmforce), messageToIntent)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        })
}

fun Fragment.successTransactionReceived(
    messageToShow: String = getString(R.string.success_transaction_recieved),
    messageToIntent: String = getString(R.string.success_transaction_recieved)
) {
    requireActivity().sweetAlertDialogSuccessType(
        messageToShow.toString(),
        action = {
            val intent = Intent()
            intent.putExtra(
                getString(R.string.status_code_message_to_farmforce),
                messageToIntent
            )
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        }
    )
}

fun Fragment.errorCustomerFailure(
    message: String
) {
    requireActivity().sweetAlertDialogWarningType(
        message.toString(),
        action = {
            val intent = Intent()
            intent.putExtra(getString(R.string.status_code_message_to_farmforce), message)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        })
}