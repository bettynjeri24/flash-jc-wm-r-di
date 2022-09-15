package com.ekenya.rnd.common.utils.custom

import android.text.TextUtils
import androidx.core.text.isDigitsOnly
import java.util.regex.Pattern

/**
 * Extension Function to convert string to ascii
 */
fun String.stringToAsciiNumber(): String {
    var asciiStr = ""
    val iterator = iterator()
    for (i in iterator) {
        asciiStr += (i.toInt().toString() + ",")
    }
    return asciiStr.removeSuffix(",")
}

/**
 * Extension Function to convert string to ascii
 */
fun String.stringToAscii(): String {
    var s = ""
    this.forEach { s = s + it.toInt() + "," }

    return s.removeSuffix(",")
}

fun String.isPhoneNumber() =
    length in 9..10 && all { it.isDigit() }

fun isMaskString(input: String): Boolean {
    var result = true
    if (result) {
        maskString(input = input)
    } else {
        result = false
    }
    return result
}

fun maskString(input: String): String {
    val length = if (input.length < 4) {
        (input.length - input.length / 2)
    } else {
        (input.length - input.length / 4)
    }
    val s = input.substring(0, length)
    return s.replace("[A-Za-z0-9]".toRegex(), "X") + input.substring(length)
}

/**
 * -? – We check if the number has zero or one minus (“-“) symbol at the start.
[0-9] – We check if there are one or more digits in a String.
This fails if there are no numbers.
Notably, this only matches 0, 1, 2, 3, 4, 5, 6, 7, 8, and 9.
However, if we want to look for other Unicode numerals, we can use “\d” instead of “[0-9]”.
(\\.[0-9]+)? – We check if there’s a decimal (“.”) symbol; if so, it must be followed by at least one digit.
val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
 */
fun isNumeric(toCheck: String): Boolean {
    val regex = "^[0-9]$".toRegex()
    val regexStr = "^[0-9]*$"
    // return toCheck.matches(regex)
    return toCheck.isDigitsOnly()
}

/**
 * Is valid pin boolean.
 *
 * @param pin the pin
 * @return the boolean
 */
fun isValidPIN(pin: String?): Boolean {
    return if (pin.isNullOrEmpty()) false else {
        return Pattern.matches("^[0-9]{4}$", pin)
    }
    // return Pattern.matches("^\\d{4}\$", pin)
}

/**
 * Extension function to check if PIN is weak
 * Return true if PIN is weak
 */
fun String.isWeakPin(): Boolean {
    // check if all characters are same
    val s1: MutableSet<Char> = HashSet()
    // Insert characters in the set
    for (i in 0 until this.length) s1.add(this[i])
    if (s1.size == 1) return true // pass cannot contain 4 consecutive same characters
    if (s1.size - 1 == 1) return true // pass cannot contain 3 consecutive same characters
    // Check for three consecutive digits in pin
    var prev: Char? = null
    var asc: Boolean? = null
    var streak = 0
    for (c in this.toCharArray()) {
        if (prev != null) {
            when (c - prev) {
                -1 -> if (java.lang.Boolean.FALSE == asc) streak++ else {
                    asc = false
                    streak = 2
                }
                1 -> if (java.lang.Boolean.TRUE == asc) streak++ else {
                    asc = true
                    streak = 2
                }
                else -> {
                    asc = null
                    streak = 0
                }
            }
            if (streak == 3) return true // 3 consecutive characters, pass is weak
        }
        prev = c
    }
    return false // the pass is strong
}

fun isValidAmount(amount: String?): Boolean {
    return if (amount.isNullOrEmpty()) false else {
        return try {
            (
                Pattern.matches("^[0-9]{2,6}$", amount) &&
                    amount.toInt() > 5
                )
        } catch (ex: NumberFormatException) {
            false
        }
    }
}

fun isValidOTP(amount: String?): Boolean {
    return if (amount.isNullOrEmpty()) false else {
        return try {
            (
                Pattern.matches("^[0-9]{4,6}$", amount)
                )
        } catch (ex: NumberFormatException) {
            false
        }
    }
}

fun isValidName(narration: String): Boolean {
    return !((TextUtils.isEmpty(narration) || narration.length < 3)) && Pattern.matches(
        "^[A-Za-z äãåāàâæáėęēêéèëÿūúũùûüįíĩīïîìōóõòöôœčçćñÄÃÅĀÀÂÆÁĖĘĒÊÉÈËŸŪÚŨÙÛÜĮÍĨĪÏÎÌŌÓÕÒÖÔŒČÇĆÑ]+\\w$",
        narration
    )
}

// validate name
fun isValidComments(narration: String): Boolean {
    return !((TextUtils.isEmpty(narration) || narration.length < 3))
}

fun isValidDate(narration: String): Boolean {
    return !((TextUtils.isEmpty(narration) || narration.length < 3)) && Pattern.matches(
        "^[0-9-]+\\w$",
        narration
    )
}

fun isValidProvisionalNationalID2(selectedPosition: Int, idType: String?): Boolean {
    return if (idType.isNullOrBlank()) false else when (selectedPosition) {
        0, 4 -> {
            Pattern.matches("^[0-9]{17}$", idType)
        }
        1 -> {
            Pattern.matches("^[0-9]{9}$", idType)
        }
        2, 3 -> {
            Pattern.matches("^[a-zA-Z0-9]{10}$", idType)
        }
        5 -> {
            Pattern.matches("^[a-zA-Z0-9]{20}$", idType)
        }
        6 -> {
            Pattern.matches("^[0-9]{11}$", idType)
        }
        else -> {
            false
        }
    }
}

fun isValidPhone(phone: String?): Boolean {
    return if (phone.isNullOrEmpty()) false else {
        return Pattern.matches("^0[0-9]{9,10}$", phone) // |[0-9]{10}
    }
    // for camerron test "^07[0-9]{8}|01[0-9]{8}$", phone 0708396044
}

fun formatPhoneNumber(phone: String): String {
    var formatedPhone = phone.replace(" ".toRegex(), "")
    when (formatedPhone.length) {
        // cater for old contacts
        8 -> {
            formatedPhone = formartOldContact(formatedPhone)
        }
        13 -> {
            formatedPhone = "0" + formatedPhone.substring(4)
        }
        11 -> {
            formatedPhone = formartOldContact(formatedPhone.substring(3))
        }
        10 -> {
            return formatedPhone
        }
    }
    return formatedPhone
}

private fun formartOldContact(formatedPhone: String): String {
    lateinit var oldContact: String
    var firstInitials = formatedPhone.take(2)
    when (firstInitials) {
        // mtn old code
        "05" -> { // ,"04","06","44","45","46"
            oldContact = "$firstInitials$formatedPhone"
        }
        "07" -> { // ,"08","09","47","48","49"
            oldContact = "$firstInitials$formatedPhone"
        }
        else -> {
            oldContact = formatedPhone
        }
    }
    return oldContact
}

fun isValidPhoneWithCode(phone: String?): Boolean {
    return if (phone.isNullOrEmpty()) false else {
        return Pattern.matches("^[0-9]{13}|[0-9]{12}$", phone)
    }
    // check this restriction by changing {8} "^7[0-9]{8}|1[0-9]{8}$"
}

class FieldValidation {

    // vaidate phone number
    fun validPhoneNUmber(phoneNumber: String, transType: String): String {
        var message = VALIDINPUT
        if (phoneNumber.trim().isNotEmpty()) {
            if (phoneNumber.trim().length != 10 && phoneNumber.trim().length < 12) {
                message = "Provide a valid phone number"
            }
            if (phoneNumber.trim().length == 10 && !phoneNumber.trim().startsWith("225")) {
                message = "Provide a valid phone number"
            }
        } else {
            message = "Provide a valid phone number"
        }
        return message
    }

    // validate amount
    fun validAmount(amount: String, transType: String): String {
        var message = VALIDINPUT
        if (!amount.contentEquals("")) {
            if (amount.toInt() < 10) {
                message = "$transType Amount cannot be less than 10"
            }
            if (amount.toInt() > 100000) {
                message = REQUESTAPPROVAL
            }
        } else {
            message = "$transType Amount cannot be zero"
        }

        return message
    }

    // check transaction amount against available balance
    fun checkIfTransAmntEnough(availableAmt: Int, amount: String): String {
        var message = SUFFICIENTAMOUNT
        if (!amount.contentEquals("")) {
            if (amount.toInt() > availableAmt) {
                message = " Insufficient amount"
            }
            if (amount.toInt() > 100000) {
                message = REQUESTAPPROVAL
            }
        }
        return message
    }

    // validate name
    fun validName(contentStr: String, fieldName: String): String {
        var message = VALIDINPUT
        if (contentStr.trim().length < 3) {
            message = "Provide a valid $fieldName name"
        }
        return message
    }

    // validate amount
    fun validIdNumber(iDcontent: String, fieldName: String): String {
        var message = VALIDINPUT
        if (!iDcontent.contentEquals("")) {
            if (iDcontent.length < 8) {
                message = "Provide a valid $fieldName"
            }
            /*if (iDcontent.toInt()>100000){
                message = REQUESTAPPROVAL
            }*/
        } else {
            message = "Provide a valid $fieldName"
        }
        return message
    }

    // validate amount
    fun validAccNumber(accNumber: String, fieldName: String): String {
        var message = VALIDINPUT
        if (!accNumber.contentEquals("")) {
            if (accNumber.length < 10) {
                message = "Provide a valid $fieldName"
            }
            /*if (accNumber.toInt()>1000000){
                message = REQUESTAPPROVAL
            }*/
        } else {
            message = "Provide a valid $fieldName"
        }
        return message
    }

    // Validate Reference number
    fun validRefNumber(accNumber: String, fieldName: String): String {
        var message = VALIDINPUT
        if (!accNumber.contentEquals("")) {
            if (accNumber.isEmpty() && accNumber.trim().length < 5) {
                message = "Provide a valid $fieldName"
            }
            /*if (accNumber.toInt()>1000000){
                message = REQUESTAPPROVAL
            }*/
        } else {
            message = "Provide a valid $fieldName"
        }
        return message
    }

    companion object {
        val VALIDINPUT = "Valid input"
        val REQUESTAPPROVAL = "REQUEST APPROVAL"
        val SELECTACCOUNT = "Select Account"
        val SUFFICIENTAMOUNT = "Sufficient limit"
    }
}
