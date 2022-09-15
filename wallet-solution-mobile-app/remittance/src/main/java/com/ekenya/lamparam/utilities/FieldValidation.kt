package com.ekenya.lamparam.utilities

import com.google.android.material.textfield.TextInputEditText

class FieldValidation {

    //vaidate phone number
    fun validPhoneNUmber(phoneNumber:String,transType:String):String{
        var message = VALIDINPUT
        if (phoneNumber.trim().isNotEmpty()){
            if (phoneNumber.trim().length !=10 && phoneNumber.trim().length <12){
                message = "Provide a valid phone number"
            }
            if (phoneNumber.trim().length == 10 && !phoneNumber.trim().startsWith("245")){
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
       if (contentStr.length <3){
           message = "Provide a valid $fieldName name"
       }

       val nameparts = contentStr.split(" ")
       if (nameparts.size<2){
           message = "Please provide more than one name"
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
        return message
    }


    companion object{
        val VALIDINPUT = "Valid input"
        val REQUESTAPPROVAL = "REQUEST APPROVAL"
        val SELECTACCOUNT = "Select Account"
        val SUFFICIENTAMOUNT = "Sufficient limit"
    }
}