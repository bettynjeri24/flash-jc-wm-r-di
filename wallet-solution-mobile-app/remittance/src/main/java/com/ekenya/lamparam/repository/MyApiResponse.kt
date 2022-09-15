package com.ekenya.lamparam.repository



class MyApiResponse(
    val requestName: String = "",
    val responseObj: Any? = null,
    val isSuccessfull:Boolean,
    val code:Int
){
   fun getMessage():String
   {
        return getMessagebyCode(code)
   }

    fun getMessagebyCode(code: Int = 300):String{
        var message: String
        if (code == 401) {
            message = "expired token"
        } else if (code == 400) {
            message = "server error"
        } else if (code in 400..499) {
            message = "Error creating connection please try again"
        } else if (code in 500..599) {
            message = "We are having an issue accessing our servers please try again later"
        }else if (code == 300) {
            message = "Please check your internet connection and try again"
        } else {
            message = "Unexpected error occurred"
        }
        return message
    }
}