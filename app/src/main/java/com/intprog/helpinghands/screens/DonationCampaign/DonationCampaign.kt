package com.intprog.helpinghands.screens.DonationCampaign

import android.net.Uri

data class DonationCampaign(
    val title: String, val description: String, val amountNeeded: String,    val imageUri: Uri? = null
)