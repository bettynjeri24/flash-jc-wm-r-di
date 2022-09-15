package io.eclectics.cargilldigital.utils

import android.app.Activity
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.AppCargillDigital
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.printer.PrinterActivity
import io.eclectics.cargilldigital.utils.UtilPreference.Companion.sectionCode
import io.eclectics.cargilldigital.utils.UtilPreference.Companion.userSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat
import java.util.*


class GlobalMethods {

    fun simpleMasking(accNo: String, fnDigit: Int, lnDigit: Int): String {
        var accLength = accNo.length
        var first4 = accNo.take(fnDigit)
        var last4 = accNo.takeLast(lnDigit)
        var maskedLengh = accLength - fnDigit - lnDigit
        var maskasteric = StringBuilder(maskedLengh)
        for (i in 0 until maskedLengh) {
            maskasteric.append('*')
        }
        return "${first4}$maskasteric$last4"
    }

    fun transactionWarning(activity: FragmentActivity, message: String) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning!"
            contentText = message
            setCancelable(false)
            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                }
            }
            show()

        }
    }

    fun pinTransactionWarning(activity: FragmentActivity, message: String) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning!"
            contentText = message
            setCancelable(false)
            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    (activity as MainActivity?)!!.navigationMgmt()
                }
            }
            show()

        }
    }

    fun confirmTransactionEnd(
        message: String,
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                }
            }
            setCancelable(false)

            show()

        }

    }


    fun loader(
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.apply {
            titleText = "Sending request"
            setCancelable(false)
            show()
        }

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            progressDialog.dismiss()
        }
    }

    fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

    fun getBigSizeSpanned(text: String): String {
        return "<b>$text</b>"
    }

    fun getSmallSizeSpanned(text: String): String {
        return "<s>$text</s>"
    }

    /**
     * Get greetings string.
     *
     * @return the string
     */
    fun getGreetings(): String {
        //Get the time of day
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        //Set greeting
        return when (cal.get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {
                AppCargillDigital.applicationContext().getString(R.string.greetings_afternoon)
            }
            in 17..20 -> {
                AppCargillDigital.applicationContext().getString(R.string.greetings_evening)
            }
            in 21..23 -> {
                AppCargillDigital.applicationContext().getString(R.string.greetings_night)
            }
            else -> {
                AppCargillDigital.applicationContext().getString(R.string.greetings_morning)
            }
        }
    }

    fun createDate(date: String): DateTime {
        val format = DateTimeFormat.forPattern("yyyy-MM-dd")
        return format.parseDateTime(date)
    }

    object navigation {
        fun options(): NavOptions {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.slide_out)
                .setPopEnterAnim(R.anim.slide_in)
                .setPopExitAnim(R.anim.slide_out)
                .build()

            return navOptions
        }

        fun Fragment.spinnerDatePicker(
            tv: EditText,
            defaultDate: DateTime,
            title: String,
            minDate: DateTime? = null,
            maxDate: DateTime? = null
        ) {
            val builder = SpinnerDatePickerDialogBuilder()
                .context(requireContext())
                .callback { _, year, monthOfYear, dayOfMonth ->
                    val month = if (monthOfYear <= 9) {
                        "0${monthOfYear + 1}"
                    } else {
                        "${monthOfYear + 1}"
                    }
                    val day = if (dayOfMonth <= 9) {
                        "0$dayOfMonth"
                    } else {
                        "$dayOfMonth"
                    }
                    val dateStr = "$year-$month-$day"
                    tv.setText(dateStr)
                }
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .customTitle(title)
                .showDaySpinner(true)
                .defaultDate(
                    defaultDate.year,
                    defaultDate.monthOfYear.minus(1),
                    defaultDate.dayOfMonth
                )

            if (minDate != null && maxDate != null) {
                builder.minDate(minDate.year, minDate.monthOfYear.minus(1), minDate.dayOfMonth)
                builder.maxDate(maxDate.year, maxDate.monthOfYear.minus(1), maxDate.dayOfMonth)
            } else if (maxDate != null) {
                builder.maxDate(maxDate.year, maxDate.monthOfYear.minus(1), maxDate.dayOfMonth)
            } else if (minDate != null) {
                builder.minDate(minDate.year, minDate.monthOfYear.minus(1), minDate.dayOfMonth)
            }

            builder.build().show()
        }
        /**
         * Test formatt
         */
        /**
         * Round off to 2dp
         * and add comma separator.
         * @param wholeNumber
         * @return
         */
        fun formatNumber(wholeNumber: String): String? {
            val number = wholeNumber.toDouble()

            //NumberFormat.getInstance().format(myNumber);
            val formatter = DecimalFormat("# ##0.00")
            //NumberFormat formatter =NumberFormat.getInstance();
            return formatter.format(number)
        }

        private fun getHeader(copyOwner: String): Array<String>? {
            return arrayOf(
                """
            ${PrinterActivity.centeredText("***** $copyOwner *****")}
            
            """.trimIndent(),
                "\n",
                """
            ${PrinterActivity.centeredText("Cargill Digital Payment")}
            
            """.trimIndent(),
                """
            ${PrinterActivity.centeredText("Code: " + sectionCode)}
            
            """.trimIndent(),
                "\n",
                """
            DATE        TIME        TERMINAL ID
            
            """.trimIndent(),
                """${NetworkUtility().getUTCtime()}     122511415
""",  // "CODE: " + Config.AGENT_CODE + "\n",
                """
            TRAN NUM: 5225152525
            
            """.trimIndent(),
                """
            SECTION: $userSection
            
            """.trimIndent(),
                PrinterActivity.getSeparator("<<SEPARATOR>>")
            )
        }

    }

    companion object {
        var countryCode = "225"
        var ussdShortCode = "9144"//"605*214"
        fun currentTime() {

        }
    }
}

fun Activity.sweetAlertDialogSuccessType(
    message: String,
    action: (() -> Unit)? = null
) {
    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText("Success")
        .setContentText(message)
        .setConfirmText("Okay")
        .setConfirmClickListener { sDialog ->
            action?.invoke()
            sDialog.dismissWithAnimation()
        }
        .show()

}

fun Activity.sweetAlertDialogWarningType(
    message: String,
    action: (() -> Unit)? = null
) {
    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText("Error")
        .setContentText(message)
        .setConfirmText("Okay")
        .setConfirmClickListener { sDialog ->
            action?.invoke()
            sDialog.dismissWithAnimation()
        }
        .show()

}

fun Activity.sweetAlertDialogWithCancelAndConfirm(
    message: String,
    actionCancel: (() -> Unit)? = null,
    actionConfirm: (() -> Unit)? = null
) {
    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(getString(R.string.directives))
        .setContentText(message)
        .setCancelText(getString(R.string.cancel))
        .setConfirmText(getString(R.string.confirm))
        .showCancelButton(true)
        .setCancelClickListener { sDialog ->
            actionCancel?.invoke()
            sDialog.dismissWithAnimation()
        }
        .setConfirmClickListener { sDialog ->
            actionConfirm?.invoke()
            sDialog.dismissWithAnimation()
        }
        .show()
}