// Campaign.kt
package com.intprog.helpinghands.models

data class Campaign(
    val title: String ="",
    val imageUri: String?="",
    val type: CampaignType = CampaignType.UNSPECIALIZED
)

enum class CampaignType {
    VOLUNTEER,
    DONATION,
    UNSPECIALIZED
}

// Add these data classes for campaign details
data class VolunteerCampaignDetails(
    val title: String,
    val category: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val age: String,
    val location: String,
    val imageUri: String?,
    val type: CampaignType
)

data class DonationCampaignDetails(
    val title: String,
    val description: String,
    val amountNeeded: String,
    val category: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val contactMethod: String,
    val imageUri: String?,
    val type: CampaignType
)

data class UnspecializedActivityDetails(
    val title: String,
    val noOfParticipants: String,
    val description: String,
    val imageUri: String?,
    val type: CampaignType
)
