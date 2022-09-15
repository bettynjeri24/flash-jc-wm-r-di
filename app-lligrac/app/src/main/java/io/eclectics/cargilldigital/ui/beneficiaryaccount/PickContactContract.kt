package io.eclectics.cargilldigital.ui.beneficiaryaccount

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContract
import timber.log.Timber

class PickContactContract : ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        Timber.e("TAG createIntent() called")
        return Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI).also {
            it.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }

    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        Timber.e("parseResult() called")
        return if (resultCode != Activity.RESULT_OK || intent == null) null else intent.data
    }
}