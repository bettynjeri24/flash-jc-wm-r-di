package io.eclectics.cargilldigital.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.R


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
        var context = AppCargillDigital.applicationContext().applicationContext
        if(amount.isNotEmpty()){
            if(amount.toInt() > UtilPreference().getWalletBalance(context).toInt()){
                editText.error = context.resources.getString(R.string.inssufficient_funds_sbttl)
            }
        }
    }

}
