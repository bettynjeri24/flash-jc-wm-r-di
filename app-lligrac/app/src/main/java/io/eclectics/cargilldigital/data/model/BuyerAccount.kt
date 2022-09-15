package io.eclectics.cargilldigital.data.model

object BuyerAccount {
    fun getPaymentOptions(): ArrayList<PaymentOptions> {
        var optionList =ArrayList<PaymentOptions>()
        var otpion1 = PaymentOptions(1,"Cocoa Beans")
        var otpion2 = PaymentOptions(2,"Premium payment")
        var otpion3 = PaymentOptions(3,"Loan disbursement")

        optionList.add(otpion1)
        optionList.add(otpion2)
        optionList.add(otpion3)

        return optionList
    }

    data class PaymentOptions(
        var optionId:Int,
        var optionName:String
    )
}