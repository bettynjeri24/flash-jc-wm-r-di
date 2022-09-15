package io.eclectics.cargilldigital.data.model

object ChargesModel {
    class TransCharges(
        var minCharge:Int,
        var maxCharge:Int,
        var chargeAmt:Int
    )
    fun getChargesAmount(amount:Int):Double{
        var chargesPercentage = 0.01
        return amount * chargesPercentage
    }
    fun getTotalBookingAmount(amount:Int):Double{
        var chargesPercentage = 0.99
        return (amount * 1)/0.99
    }
    fun getChargeList():ArrayList<TransCharges>{
        var arrayList = ArrayList<TransCharges>()
        var charg1 = TransCharges(0,499,50)
        var charg2 = TransCharges(500,1499,80)
        var charg3 = TransCharges(1500,4999,250)
        var charg4 = TransCharges(5000,9999,360)
        var charg5 = TransCharges(10000,20000,1800)

        arrayList.add(charg1)
        arrayList.add(charg2)
        arrayList.add(charg3)
        arrayList.add(charg4)
        arrayList.add(charg5)
        return arrayList
    }
    fun getCharges(chargeAmt: Int):Int{
        var chargList = getChargeList()
        var amount = 0
        for(charg in chargList){
            if(chargeAmt in charg.minCharge .. charg.maxCharge){
                amount = charg.chargeAmt
                break
                //return amount
            }
        }
        return 0//amount
    }
}