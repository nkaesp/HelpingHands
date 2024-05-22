// Campaign.kt
package com.intprog.helpinghands.models

data class Campaign(
    val title: String,
    val imageUri: String,
    val type: CampaignType,
)

enum class CampaignType {
    VOLUNTEER,
    DONATION,
    UNSPECIALIZED
}
