package com.ekenya.rnd.tijara.utils

import android.util.Patterns
import timber.log.Timber

class FieldValidators {
    //vaidate phone number
    fun validPhoneNUmber(phoneNumber: String):String{
        var message = VALIDINPUT
        if (phoneNumber.trim().isNotEmpty()){
            if (phoneNumber.trim().length !=10){
                message = "Enter valid phone number e.g(0712345678 0r 0110123456)"
            }
           /* if (phoneNumber.trim().length !=12 && !phoneNumber.trim().startsWith("254")){
                message = "Provide a valid  number"
            }*/
           /* if (phoneNumber.trim().length == 10 && !phoneNumber.trim().startsWith("254")){
                message = "Provide a v phone number"
            }*/

        }else{
            message = "Phone number cannot be blank"
        }
        return message
    }


    fun formatPhoneNumber(unformattedPhone: String):String{
        val formatPhone:String
       if (unformattedPhone.startsWith("254")){
            formatPhone = "0"+unformattedPhone.substring(3)
        }else if (unformattedPhone.startsWith("+254")){
            formatPhone="0"+unformattedPhone.substring(4)
        }
        else {
            formatPhone=unformattedPhone
        }
        return formatPhone
    }

    fun formatContact(unformatContact:String):String{
        val formatedPhone:String
        if (unformatContact.startsWith("+254")){
            formatedPhone="0"+(unformatContact.substring(4))
            Timber.d("FORMATED PHONE $formatedPhone")
        }else if(unformatContact.startsWith("254")){
            formatedPhone="0"+(unformatContact.substring(3))
            Timber.d("FORMAT PHONE $formatedPhone")
        }else {
            formatedPhone=unformatContact
            Timber.d("FORMATPHONE $formatedPhone")
        }
        return formatedPhone
    }

    /**
     * valid email address
     */
    fun isEmailValid(email: String): String {
        var message = VALIDINPUT
        if (email.trim().isNotEmpty()){
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                message="Provide a valid Email Address"
            }
        }else{
            message="Email Address cannot be blank"
        }
        return  message
    }

    /**
     * valid national id
     */
    fun validNationalID(iDNumber: String):String{
        var message = VALIDINPUT
        if (iDNumber.trim().isNotEmpty() ){
            if (iDNumber.length<8){
                message = "Provide a valid national ID"
            }
        } else{
            message = "Provide a valid national ID"
        }
        return message
    }
    fun validBankAccount(bankAccount: String):String{
        var message = VALIDINPUT
        if (bankAccount.trim().isNotEmpty() ){
            if (bankAccount.length<16){
                message = "Provide a valid account number"
            }
        } else{
            message = "Provide a valid account number"
        }
        return message
    }

    /**
     * valid names
     */
    fun validName(name:String):String{
        var message = VALIDINPUT
        if (name.trim().isNotEmpty()){
        if (name.length <4){
            message = "Provide a valid username"
        }
    }else{
        message="Username cannot be blank"
    }
        return message

    }
    companion object{
        var message="Provide a valid phone number"
        val VALIDINPUT = "Valid input"
        val REQUESTAPPROVAL = "REQUEST APPROVAL"
        val SELECTACCOUNT = "Select Account"
        val SUFFICIENTAMOUNT = "Sufficient limit"
    }
}