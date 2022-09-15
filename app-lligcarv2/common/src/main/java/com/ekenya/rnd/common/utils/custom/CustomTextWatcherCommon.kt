package com.ekenya.rnd.common.utils.custom

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.ekenya.rnd.common.R
import com.google.android.material.textfield.TextInputLayout


class CustomTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {
    override fun beforeTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
        textInputLayout.error = null
    }

    override fun afterTextChanged(editable: Editable) {}

}

class CustomTextWatcher2(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
        editText.error = null
    }

    override fun afterTextChanged(editable: Editable) {}

}

class CustomTextWatcherAmount(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
    }

    override fun onTextChanged(
        charSequence: CharSequence,
        i: Int,
        i1: Int,
        i2: Int
    ) {
        editText.error = null
    }

    override fun afterTextChanged(editable: Editable) {
        var amount = editText.text.toString()
        var context = editText.context
        if (amount.isNotEmpty()) {
            if (amount.toInt() > 10000) {
                editText.error = context.resources.getString(R.string.inssufficient_funds_sbttl)
            }
        }
    }

}
