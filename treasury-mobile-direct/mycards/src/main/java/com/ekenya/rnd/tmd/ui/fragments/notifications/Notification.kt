package com.ekenya.rnd.tmd.ui.fragments.notifications

data class Notification(
    val title: String,
    val content: String
)

val notification = mutableListOf<Notification>().apply {
    add(Notification("New issues on offer", "Dear Investor, New Bills on offer are 2351/091, 2324/182 and 2270/364 Value Date 13/01/2020. Period of sale 03/01/2020 to 08/01/2020"))
    add(Notification("Redemption", "Dear Investor, your REDEMPTION for issue 2512/364 has been paid to your Bank Account No ending … on 06-01-2020"))
    add(Notification("Coupon", "Dear Investor, your COUPON for issue FXD1/2019/005 has been paid to your Bank Account No ending … on 06-01-2020"))
    add(Notification("Refund", "Dear Investor, your Rollover REFUND for issue FXD7/2019/010 has been paid to your Bank Account No ending … on 06-01-2020"))
    add(Notification("General", "Dear Investor, Central Bank of Kenya has rolled out Treasury Mobile Direct services which is designed to facilitate investment in Government Securities through the mobile telephone. For more information, kindly visit our Branches and Currency Centres or website www.centralbank.go.ke Enquires Call 540202860000/+254717002013."))
}
