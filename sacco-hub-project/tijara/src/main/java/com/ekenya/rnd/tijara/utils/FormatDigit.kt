package com.ekenya.rnd.tijara.utils

import com.ekenya.rnd.common.Constants
import java.lang.NumberFormatException
import java.text.DecimalFormat

class FormatDigit {
    companion object {
        fun formatDigits(wholeNumber: String): String {
            var number: Double = 0.0
            val formatter: DecimalFormat
            try {
                number = wholeNumber.toDouble()
            } catch (e: NumberFormatException) {
                return wholeNumber
            }
            if (number==0.00){
                 formatter = DecimalFormat("0.00")
            }else{
                 formatter = DecimalFormat("#,###.00")
            }

            return formatter.format(number)
        }


         fun getFormattedCurrency(): String {
            var currency: String = "KES"
            if (currency.isEmpty()) {
                return "KES"
            }
            return currency
        }

        fun getNumberFormatted(number: String): String {
            return String.format("${getFormattedCurrency()} %.2f", number.toDouble())
        }

        fun formatPercentage(number: String): String {
            return String.format("%.2f", number.toDouble())
        }
        fun splitTwoString(value:String):String{
            val splited: List<String> =value.split("\\s".toRegex())
            return splited[1]
        }
    }
}
