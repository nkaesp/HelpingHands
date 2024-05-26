package com.intprog.helpinghands.screens.DonationCampaign

data class DonationPost(
    val title: String,
    val description: String,
    val amountNeeded: String,
    val category: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val contactMethod: String,
    val imageUri: String
)