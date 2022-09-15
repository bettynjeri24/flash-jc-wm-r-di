package com.ekenya.rnd.dashboard.utils

import android.content.Context
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.datadashboard.model.*

object DashBoardUtils {
    fun getUserLoginData(context: Context?): LoginData {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }

        /*return LoginData("Authenticate",
            "254700124573","1234",
            grant_type ,
            geolocation,
            userAgentVersion,
            userAgent)*/
        return com.ekenya.rnd.dashboard.datadashboard.model.LoginData(
            "Authenticate",
            phoneNumber!!,
            pin!!,
            "access_token",
            "Home",
            "22 (5.1.1)",
            "android"
        )

    }

    fun getWalletRequestData(context: Context?): AccountBalanceRequest {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val accountNumber = context?.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }


        return com.ekenya.rnd.dashboard.datadashboard.model.AccountBalanceRequest(
            "AccountBalance",
            0,
            1,
            phoneNumber!!,
            false,
            accountNumber!!, "Home"
        )

    }

    fun getMinistatementRequestData(context: Context?): MinistatementRequest {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val accountNumber = context?.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }


        return com.ekenya.rnd.dashboard.datadashboard.model.MinistatementRequest(
            "MiniStatement",
            0,
            1,
            phoneNumber!!,
            false,
            accountNumber!!, "Home",
            "android",
            "5.1"
        )

    }

    fun topUpData(context: Context?): TopUpRequest {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val amount = context?.let { it1 -> SharedPreferencesManager.getAmouttoTopUP(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val accountNumber = context?.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }



        return com.ekenya.rnd.dashboard.datadashboard.model.TopUpRequest(
            "DepositMoneyMpesa",
            Integer.parseInt(amount!!),
            2,
            "2",
            phoneNumber!!,
            phoneNumber!!,
            false,
            "",
            accountNumber!!,
            "Home",
            "android",
            "5.1",
            "",
            "",
            ""

        )

    }

    fun topUpWalletFromCard(context: Context?, stage: String): TopUpRequest {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val amount = context?.let { it1 -> SharedPreferencesManager.getAmouttoTopUP(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val accountNumber = context?.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }
        val card = context?.let { it1 -> SharedPreferencesManager.getcardDetails(it1) }



        return com.ekenya.rnd.dashboard.datadashboard.model.TopUpRequest(
            "CardDeposit",
            Integer.parseInt(amount!!),
            3,
            "2",
            phoneNumber!!,
            phoneNumber!!,
            false,
            stage,
            accountNumber!!,
            "Home",
            "android",
            "5.1",
            card!!,
            "",
            ""
        )
        //"FuxbcHHjgdMVEOlGu32SjHrbgS/VNkq1GSUJo/hPKC79TduaYQCj/maTYp7TSk0CSqPtfMsQDoW7fwQL111MJVcdhSPFhhmfxQfnfecEmvuuMCN3u7dVJy79KWmrRkm+WzeTpbEHvLDlPsqvdQymSOQmfeE9OTtOx9oSH3G1/nXM0+j8NH02F/Ipgz9yOZzEma6VhdSr3SpLdFLjby1+IwQrdw8fp1ks6z5Hhl9wXizTvGII3/5g9JqEQQYlCP/hMAfNEFZ1KYexXiDjOTCHOhs5t9N3kf8JK/vkyH9GWgq9gqrr2a9DlZV+OT0DzRSxX+rzUs6OSjj3IyazO3sZBg==")

    }

    fun sendMoneytoAnotherWallet(context: Context?): SendMoneyToAnotherWalletRequest {

        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val amount = context?.let { it1 -> SharedPreferencesManager.getAmouttoTopUP(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val grant_type = context?.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGent(it1) }
        val pin = context?.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val accountNumber = context?.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }



        return com.ekenya.rnd.dashboard.datadashboard.model.SendMoneyToAnotherWalletRequest(
            "SendMoney",
            Integer.parseInt(amount!!),
            1,
            phoneNumber!!,
            phoneNumber!!,
            false,
            " ",
            "TA2459482000062",
            "TA2459485000072",
            accountNumber!!,
            "Home",
            "android",

            )

    }
}