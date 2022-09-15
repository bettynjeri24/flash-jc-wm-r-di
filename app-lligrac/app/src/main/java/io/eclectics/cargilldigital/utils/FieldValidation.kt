package io.eclectics.cargilldigital.utils

import android.text.TextUtils
import androidx.core.text.isDigitsOnly
import java.util.regex.Pattern

fun isNumeric(toCheck: String): Boolean {
    val regex = "^[0-9]$".toRegex()
    val regexStr = "^[0-9]*$"
    // return toCheck.matches(regex)
    return toCheck.isDigitsOnly()
}


object InputValidator{
    /**
     * Is valid pin boolean.
     *
     * @param pin the pin
     * @return the boolean
     */
    fun isValidPIN(pin: String?): Boolean {
        return if (pin.isNullOrEmpty()) false else
            return Pattern.matches("^[0-9]{4,5}$", pin)
    }
    fun isValidAmount(amount: String?): Boolean {
        return if (amount.isNullOrEmpty()) false else
            return try {
                (Pattern.matches("^[0-9]{2,6}$", amount)
                        && amount.toInt() > 5)
            } catch (ex: NumberFormatException) {
                false
            }
    }
    fun isValidOTP(amount: String?): Boolean {
        return if (amount.isNullOrEmpty()) false else
            return try {
                (Pattern.matches("^[0-9]{4,6}$", amount)
                      )
            } catch (ex: NumberFormatException) {
                false
            }
    }

    fun isValidName(narration: String): Boolean {
        return !((TextUtils.isEmpty(narration) || narration.length < 3)) && Pattern.matches(
            "^[A-Za-z äãåāàâæáėęēêéèëÿūúũùûüįíĩīïîìōóõòöôœčçćñÄÃÅĀÀÂÆÁĖĘĒÊÉÈËŸŪÚŨÙÛÜĮÍĨĪÏÎÌŌÓÕÒÖÔŒČÇĆÑ]+\\w$",
            narration
        )
    }
    //validate name
    fun isValidComments(narration:String):Boolean{
        return !((TextUtils.isEmpty(narration) || narration.length < 3))
    }
    fun isValidDate(narration: String): Boolean {
        return !((TextUtils.isEmpty(narration) || narration.length < 3)) && Pattern.matches(
            "^[0-9-]+\\w$",
            narration
        )
    }

    fun isValidProvisionalNationalID2(selectedPosition: Int, idType: String?): Boolean {
        return if (idType.isNullOrBlank()) false else when(selectedPosition) {
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
        return if (phone.isNullOrEmpty()) false else
            return Pattern.matches("^0[0-9]{9}$", phone) //|[0-9]{10}
        //for camerron test "^07[0-9]{8}|01[0-9]{8}$", phone 0708396044
    }


    fun formatPhoneNumber(phone: String): String {
        var formatedPhone = phone.replace(" ".toRegex(), "")
        when (formatedPhone.length) {
            //cater for old contacts
            8 ->{
                formatedPhone = formartOldContact(formatedPhone)
            }
            13 -> {
                formatedPhone = "0" + formatedPhone.substring(4)
            }
            11 -> {
                formatedPhone =  formartOldContact(formatedPhone.substring(3))
            }
            10 -> {
                return formatedPhone
            }
        }
        return formatedPhone
    }

    private fun formartOldContact(formatedPhone: String): String {
        lateinit var oldContact:String
        var firstInitials = formatedPhone.take(2)
        when(firstInitials){
            //mtn old code
            "05" -> { //,"04","06","44","45","46"
                oldContact = "$firstInitials$formatedPhone"
            }
            "07" -> {  //,"08","09","47","48","49"
                oldContact = "$firstInitials$formatedPhone"
            }
            else ->{
                oldContact = formatedPhone
            }

        }
        return oldContact
    }


    fun isValidPhoneWithCode(phone: String?): Boolean {
        return if (phone.isNullOrEmpty()) false else
            return Pattern.matches("^[0-9]{13}|[0-9]{13}$", phone)
        //check this restriction by changing {8} "^7[0-9]{8}|1[0-9]{8}$"
    }

}

class FieldValidation {

    //vaidate phone number
    fun validPhoneNUmber(phoneNumber:String,transType:String):String{

        var message = VALIDINPUT
        if (phoneNumber.trim().isNotEmpty()){
            if (phoneNumber.trim().length !=10 && phoneNumber.trim().length <12){
                message = "Provide a valid phone number"
            }
            if (phoneNumber.trim().length == 10 && !phoneNumber.trim().startsWith("225")){
                message = "Provide a valid phone number"
            }
        }
        else{
            message = "Provide a valid phone number"
        }
        return message
    }
    //validate amount
    fun validAmount(amount:String,transType:String):String{
        var message = VALIDINPUT
        if (!amount.contentEquals("") ){
            if (amount.toInt()<10){
                message = "$transType Amount cannot be less than 10"
            }
            if (amount.toInt()>100000){
                message = REQUESTAPPROVAL
            }
        }
        else{
            message = "$transType Amount cannot be zero"
        }

        return message
    }
    
    //check transaction amount against available balance
    fun checkIfTransAmntEnough(availableAmt:Int, amount:String):String{
        var message = SUFFICIENTAMOUNT
        if (!amount.contentEquals("") ){
            if (amount.toInt()>availableAmt){
                message = " Insufficient amount"
            }
            if (amount.toInt()>100000){
                message = REQUESTAPPROVAL
            }
        }
        return message
            
    }

   //validate name
    fun validName(contentStr:String,fieldName:String):String{
       var message = VALIDINPUT
       if (contentStr.trim().length <3){
           message = "Provide a valid $fieldName name"
       }
       return message
   }
    //validate amount
    fun validIdNumber(iDcontent: String,fieldName: String):String{
        var message = VALIDINPUT
        if (!iDcontent.contentEquals("") ){
            if (iDcontent.length<8){
                message = "Provide a valid $fieldName"
            }
            /*if (iDcontent.toInt()>100000){
                message = REQUESTAPPROVAL
            }*/
        }
        else{
            message = "Provide a valid $fieldName"
        }
        return message
    }

    //validate amount
    fun validAccNumber(accNumber: String,fieldName: String):String{
        var message = VALIDINPUT
        if (!accNumber.contentEquals("") ){
            if (accNumber.length<10){
                message = "Provide a valid $fieldName"
            }
            /*if (accNumber.toInt()>1000000){
                message = REQUESTAPPROVAL
            }*/
        }
        else{
            message = "Provide a valid $fieldName"
        }
        return message
    }

    //Validate Reference number
    fun validRefNumber(accNumber: String,fieldName: String):String{
        var message = VALIDINPUT
        if (!accNumber.contentEquals("") ){
            if (accNumber.isEmpty() && accNumber.trim().length <5){
                message = "Provide a valid $fieldName"
            }
            /*if (accNumber.toInt()>1000000){
                message = REQUESTAPPROVAL
            }*/
        }
        else{
            message = "Provide a valid $fieldName"
        }
        return message
    }


    companion object{
        val VALIDINPUT = "Valid input"
        val REQUESTAPPROVAL = "REQUEST APPROVAL"
        val SELECTACCOUNT = "Select Account"
        val SUFFICIENTAMOUNT = "Sufficient limit"
    }
}