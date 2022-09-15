package com.ekenya.rnd.tmd.ui.fragments.welcome

data class Requirements(
    val title: String,
    val subtitle: String
)

val requirements = mutableListOf<Requirements>().apply {
    add(Requirements("Personal details", "Full names, Occupation, Marital Status, Gender etc."))
    add(Requirements("Identification", "Either National ID or Passport"))
    add(Requirements("Selfie", "Take a selfie or passport photo"))
    add(Requirements("Verification", "Verify your phone number and email address"))
    add(Requirements("Create your pin", "Create a 4-digit pin and confirm it for logging in"))
}
